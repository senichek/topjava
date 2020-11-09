package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController {

    @Autowired
    MealService mealService;

    @GetMapping("/meals")
    public String getMeals(Model model) {
        List<Meal> meals = mealService.getAll(SecurityUtil.authUserId());
        model.addAttribute("meals", MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/meals/{id}")
    public String getMeal(@PathVariable("id") int id, Model model) {
        Meal meal = mealService.get(id, SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/meals/{id}")
    public String update(@PathVariable("id") int id,
                         @RequestParam(value = "dateTime") String date,
                         @RequestParam(value = "description") String descr,
                         @RequestParam(value = "calories") int calories) {
        Meal toUpdate = mealService.get(id, SecurityUtil.authUserId());
        if (toUpdate != null) {
            toUpdate.setDateTime(LocalDateTime.parse(date));
            toUpdate.setDescription(descr);
            toUpdate.setCalories(calories);
            mealService.update(toUpdate, SecurityUtil.authUserId());
        }
        return "redirect:/meals";
    }

    @GetMapping("/meals/create")
    public String create() {
        return "mealForm";
    }

    @PostMapping("/meals/create")
    public String create(@RequestParam(value = "dateTime") String date,
                         @RequestParam(value = "description") String descr,
                         @RequestParam(value = "calories") int calories) {
        Meal meal = new Meal();
        meal.setDateTime(LocalDateTime.parse(date));
        meal.setDescription(descr);
        meal.setCalories(calories);
        mealService.create(meal, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @GetMapping(path = "/meals/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        mealService.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @PostMapping("/meals")
    public String getFilteredTimeDate(@RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate,
                                      @RequestParam("startTime") String startTime,
                                      @RequestParam("endTime") String endTime, Model model) {
        LocalDate startDt = parseLocalDate(startDate);
        LocalDate endDt = parseLocalDate(endDate);
        LocalTime startTm = parseLocalTime(startTime);
        LocalTime endTm = parseLocalTime(endTime);

        List<Meal> filteredByDate = mealService.getBetweenInclusive(startDt, endDt, SecurityUtil.authUserId());
        List<MealTo> filteredByTimeTos = MealsUtil.getFilteredTos(filteredByDate, SecurityUtil.authUserCaloriesPerDay(),
                startTm, endTm);
        model.addAttribute("meals", filteredByTimeTos);
        return "meals";
    }
}
