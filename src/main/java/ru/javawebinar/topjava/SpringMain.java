package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName1", "3email@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName2", "2email@mail.ru", "password", Role.USER));
            adminUserController.create(new User(null, "userName3", "1email@mail.ru", "password", Role.USER));

            // Retrieving the logged-in user
            ProfileRestController profileRestController = appCtx.getBean(ProfileRestController.class);
            User loggedInUser = profileRestController.get();

            // Creating the meals and setting these meals the ID of the logged-in user;
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);

            Meal mealOne = MealsUtil.meals.get(0);
            Meal mealTwo = MealsUtil.meals.get(1);
            Meal mealThree = MealsUtil.meals.get(2);

            mealOne.setUserId(loggedInUser.getId());
            mealTwo.setUserId(loggedInUser.getId());
            mealThree.setUserId(loggedInUser.getId());

            mealRestController.create(mealOne);
            mealRestController.create(mealTwo);
            mealRestController.create(mealThree);

            System.out.println(mealRestController.getAllMealToForSpecificUser(loggedInUser));
        }
    }
}
