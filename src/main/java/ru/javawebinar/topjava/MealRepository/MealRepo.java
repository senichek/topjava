package ru.javawebinar.topjava.MealRepository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealRepo {

    public static List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    private static AtomicInteger idCounter = new AtomicInteger(0);

    public static final int MAX_CALORIES_PER_DAY = 2000;

    public static List<MealTo> listOfMeals = new CopyOnWriteArrayList<>(MealsUtil
            .filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, MealRepo.MAX_CALORIES_PER_DAY));

    public static void delete(int id) {
        for (int i = 0; i <= listOfMeals.size(); i++) {
            if (listOfMeals.get(i).getId() == id) {
                listOfMeals.remove(i);
                return;
            }
        }
    }

    public static MealTo getMealByID(int id) {
        for (int i = 0; i <= listOfMeals.size(); i++) {
            if (listOfMeals.get(i).getId() == id) {
                return listOfMeals.get(i);
            }
        }
        return null;
    }

    public static void edit(int id, LocalDateTime dateTime, String descr, int cal) {
        MealRepo.getMealByID(id).setDateTime(dateTime);
        MealRepo.getMealByID(id).setDescription(descr);
        MealRepo.getMealByID(id).setCalories(cal);
    }

    public static void setIDs(List<MealTo> meals) {
        meals.forEach(m -> m.setId(idCounter.incrementAndGet()));
    }

    public static void recalculateExcess(List<MealTo> meals, int maxCaloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(m -> m.getDateTime()
                        .toLocalDate(), Collectors.summingInt(MealTo::getCalories)));

        for (MealTo m : meals) {
            if (maxCaloriesPerDay < totalCaloriesPerDay.get(m.getDateTime().toLocalDate())) {
                m.setExcess(true);
            } else m.setExcess(false);
        }
    }

    public static void add(MealTo meal) {
        listOfMeals.add(meal);
    }
}