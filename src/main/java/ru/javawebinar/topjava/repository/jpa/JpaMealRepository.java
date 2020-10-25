package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        meal.setUser(ref);

        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else return em.merge(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Query query = em.createNamedQuery(Meal.DELETE);
        query.setParameter("id", id);
        query.setParameter("userid", userId);
        int queryResult = query.executeUpdate();
        if (queryResult == 0) {
            throw new NotFoundException("NotFoundException");
        } else return true;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal found = new Meal();
        try {
            Query query = em.createNamedQuery(Meal.GET_BY_ID);
            query.setParameter("id", id);
            query.setParameter("userid", userId);
            found = (Meal) query.getSingleResult();
        } catch (NoResultException n) {
            throw new NotFoundException("NoFoundException");
        }
        return found;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Query query = em.createNamedQuery(Meal.ALL_BY_USER_ID);
        query.setParameter("userid", userId);
        return query.getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Query query = em.createNamedQuery(Meal.ALL_SORTED_TIME);
        query.setParameter("userid", userId);
        query.setParameter("start", startDateTime);
        query.setParameter("end", endDateTime);
        return query.getResultList();
    }
}