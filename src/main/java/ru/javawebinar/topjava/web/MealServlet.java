package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.MealRepository.MealRepo;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        List<MealTo> allMeals = MealRepo.listOfMeals;
        MealRepo.setIDs(allMeals);
        MealRepo.recalculateExcess(allMeals, MealRepo.MAX_CALORIES_PER_DAY);
        request.setAttribute("listOfMeal", allMeals);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
