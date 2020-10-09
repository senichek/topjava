package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Map<Integer, Meal>> allUsersMealRepo = new ConcurrentHashMap<>();

    @Autowired
    InMemoryUserRepository inMemoryUserRepository;

    /*{

        MealsUtil.meals.forEach(this::save);
    }*/

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            allUsersMealRepo.put(meal.getUserId(), repository);
            return meal;
        }
        // handle case: update, but not present in storage
        if (!repository.containsKey(meal.getId())) {
            throw new NotFoundException("not found");
        }
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        if (repository.containsKey(id)) {
            repository.remove(id);
            return true;
        } else return false;
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        List<Meal> all = new ArrayList<Meal>(repository.values());
        all.sort(Comparator.comparing(Meal::getDateTime));
        return all;
    }

    @Override
    public Map<Integer, Meal> getAllforSpecificUser(User user) {
        return allUsersMealRepo.get(user.getId());
    }


}

