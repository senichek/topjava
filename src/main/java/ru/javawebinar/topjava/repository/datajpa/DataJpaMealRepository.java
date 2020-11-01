package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("datajpa")
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        User user = crudUserRepository.getOne(userId);
        user.getId();

        if (meal.isNew()) {
            meal.setUser(user);
            crudRepository.save(meal);
        } else if (crudRepository.get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(user);
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = crudRepository.get(id, userId);
        if (meal == null) {
            return Boolean.parseBoolean(null);
        } else {
            crudRepository.delete(meal);
            return true;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.get(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllById(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        List<Meal> all = crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
        return all;
    }
}
