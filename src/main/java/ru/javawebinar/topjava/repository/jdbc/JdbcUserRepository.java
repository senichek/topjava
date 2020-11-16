package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.*;
import javax.validation.constraints.Email;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final SimpleJdbcInsert insertRoles;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        this.insertRoles = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_roles");

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Transactional(rollbackFor = DataAccessException.class)
    @Override
    public User save(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        Map<String, Object> roleTableColumns = new HashMap<>();

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

            user.getRoles().forEach(r -> {
                roleTableColumns.put("user_id", user.getId());
                roleTableColumns.put("role", r.name());
                insertRoles.execute(roleTableColumns);
            });
            return user;
        }

        int[] roleUpdateResult;
        int userUpdateResult;
        if (!user.isNew()) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
            userUpdateResult = namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource);

            List<Object[]> rolesParamList = new ArrayList<Object[]>();
            user.getRoles().forEach(role -> {
                rolesParamList.add(new Object[]{user.getId(), role.name()});
            });
            roleUpdateResult = jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?,?)", rolesParamList);

            if (userUpdateResult == 0 || roleUpdateResult.length == 0) {
                return null;
            }
        }
        return user;
    }

    @Transactional(rollbackFor = DataAccessException.class)
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        Collection<Role> roles = jdbcTemplate.queryForList("SELECT role from user_roles WHERE user_id=?", Role.class, id);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        if (users.size() > 0) {
            users.get(0).setRoles(roles);
        }
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(@Email String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        Collection<Role> roles = jdbcTemplate.queryForList("SELECT role from user_roles WHERE user_id=?", Role.class, users.get(0).getId());
        users.get(0).setRoles(roles);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        List<User> all = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id=ur.user_id",
                new ResultSetExtractor<List<User>>() {
            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, User> userMap = new HashMap<>();
                Collection<Role> rolesCollection = new HashSet<>();
                while (rs.next()) {
                    if (!userMap.containsKey(rs.getInt("id"))) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setName(rs.getString("name"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setRegistered(rs.getDate("registered"));
                        user.setEnabled(rs.getBoolean("enabled"));
                        user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                        rolesCollection.add(Role.valueOf(rs.getString("role")));
                        user.setRoles(rolesCollection);
                        rolesCollection.clear();
                        userMap.put(user.getId(), user);
                    } else {
                        rolesCollection.clear();
                        rolesCollection = userMap.get(rs.getInt("id")).getRoles();
                        rolesCollection.add(Role.valueOf(rs.getString("role")));
                        userMap.get(rs.getInt("id")).setRoles(rolesCollection);
                    }
                }
                return new ArrayList<>(userMap.values());
            }
        });
        return all;
    }
}
