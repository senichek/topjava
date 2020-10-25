package ru.javawebinar.topjava.repository.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JpaMealRepositoryTest {

    @Autowired
    MealRepository jpaMealRepository;

    @Test
    @Transactional
    public void save() throws Exception {
        Meal created = jpaMealRepository.save(getNewMeal(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNewMeal();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(jpaMealRepository.get(newId, USER_ID), newMeal);
    }

    @Test
    public void delete() throws Exception {
        jpaMealRepository.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> jpaMealRepository.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void get() throws Exception {
        Meal meal = jpaMealRepository.get(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(meal, meal1);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> all = jpaMealRepository.getAll(USER_ID);
        MEAL_MATCHER.assertMatch(all, meals);
    }

    @Test
    public void getBetweenHalfOpen() throws Exception {
        MEAL_MATCHER.assertMatch(jpaMealRepository.getBetweenHalfOpen(
                LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                USER_ID), meal2, meal1);
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> jpaMealRepository.delete(MealTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> jpaMealRepository.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                jpaMealRepository.save(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }

    @Test
    public void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> jpaMealRepository.get(MEAL1_ID, ADMIN_ID));
    }
}