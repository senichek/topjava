package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int USER_MEAL_ID = 100004;
    public static final int USER_ID = 100000;
    public static final int ADMIN_ID = 100001;
    public static final int NOT_FOUND = 10;


    public static final Meal userMeal = new Meal(USER_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "user_Ужин", 500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 15, 0), "new_Meal", 888);
    }

    public static Meal getUpdated() {
        Meal updated = userMeal;
        updated.setDescription("UpdatedMealDescription");
        updated.setCalories(330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
