package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        if (repository.remove(id) == null) {
            return false;
        }
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
        }

        if (!repository.containsKey(user.getId())) {
            return null;
        }
        return user;
    }

    @Override
    public User get(int id) {
        if (!repository.containsKey(id)) {
            return null;
        }
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> all = new ArrayList<User>(repository.values());
        all.sort(Comparator.comparing(User::getEmail));
        return all;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);

        for (Map.Entry<Integer, User> entry : repository.entrySet()) {
            if (entry.getValue().getEmail().matches(email)) {
                return entry.getValue();
            }
        }
        throw new NotFoundException("User with email %n not found;" + email);
    }
}
