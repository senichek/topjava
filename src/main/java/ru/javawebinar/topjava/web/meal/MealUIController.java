package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/ml/meals")
public class MealUIController extends AbstractMealController {
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void create(@RequestParam LocalDate date,
                       @RequestParam LocalTime time,
                       @RequestParam String description,
                       @RequestParam String calories) {
        Meal meal = new Meal(LocalDateTime.of(date, time), description, Integer.parseInt(calories));
        super.create(meal);
    }

    @GetMapping(value = "/filter")
    @ResponseStatus(value = HttpStatus.OK)
    public List<MealTo> filter(@RequestParam(required = false) LocalDate startDate,
                       @RequestParam(required = false) LocalDate endDate,
                       @RequestParam(required = false) LocalTime startTime,
                       @RequestParam(required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
