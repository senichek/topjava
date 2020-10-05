package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.MealRepository.MealRepo;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class EditMealServlet extends HttpServlet {
    private static final Logger log = getLogger(EditMealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        int mealId = Integer.parseInt(request.getParameter("id"));

        MealTo mealToEdit = MealRepo.getMealByID(mealId);

        request.setAttribute("mealToEdit", mealToEdit);
        request.getRequestDispatcher("edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(req.getParameter("Id"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("DateTime"));
        String descr = req.getParameter("Description");
        int cal = Integer.parseInt(req.getParameter("Calories"));

        MealRepo.edit(id, dateTime, descr, cal);
        MealRepo.recalculateExcess(MealRepo.listOfMeals, MealRepo.MAX_CALORIES_PER_DAY);
        resp.sendRedirect("meals");
    }
}
