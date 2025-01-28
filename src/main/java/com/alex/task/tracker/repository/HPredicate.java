package com.alex.task.tracker.repository;

import jakarta.persistence.criteria.Predicate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This class is a predicate-builder.
 * Then the predicate can be used inside queries to database(CRITERIA API).
 */

public class HPredicate {
    private List<Predicate> predicates = new ArrayList<>();

    public static HPredicate builder() {
        return new HPredicate();
    }

    /**
     * Returns the current {@link HPredicate instance} to continue building process or finish it.
     * If transmitted param from dto is not null then performs predicate function and result is added to predicate list.
     *
     * @param object   param from dto.
     * @param function predicate function
     * @param <T>      type of dto parameter.
     * @return current instance
     */

    public <T> HPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    /**
     * This method is analog {@link HPredicate#add(Object param, Function biPredicateFunction) addPredicate}.
     * But it allows us to realize 'OR' logic predicate.
     *
     * @param object   param1 from dto.
     * @param object1  param2 from dto.
     * @param function predicate biFunction.
     * @param <T>      type of param1.
     * @param <F>      type of param2.
     * @return current instance.
     */

    public <T, F> HPredicate addOr(T object, F object1, BiFunction<T, F, Predicate> function) {
        predicates.add(function.apply(object, object1));
        return this;
    }


    public List<Predicate> build() {
        return predicates;
    }
}