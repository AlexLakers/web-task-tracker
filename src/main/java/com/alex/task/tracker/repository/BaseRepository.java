package com.alex.task.tracker.repository;


import com.alex.task.tracker.entity.BaseEntity;
import com.alex.task.tracker.exception.RepositoryException;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.QueryHints;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * This class is base repository that implements {@link Repository repository} and realizes his methods.
 * This is a layer between {@link Repository repository} and specific repository.
 * It also contains the following state-fields:current session,entity class.
 * Thanks to this class we can create new repositories for every entity with basic 'CRUD' functional.
 * Then we can add additional specific functional using CRITERIA API,QUERYDSL or HQL using state-fields.
 *
 * @param <K> type of key entity.
 * @param <E> type of entity.
 * @see Repository repository.
 * @see BaseEntity base entity.
 */
@Getter
@AllArgsConstructor
public abstract class BaseRepository<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    private Class<E> entityClass;
    private EntityManager entityManager;

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));

    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
        entityManager.flush();

    }

    @Override
    public void delete(K id) {
        delete(findById(id).orElseThrow(() -> new RepositoryException("The account with id %1$s is not exist")));
    }

    public void delete(E entity) {
        entityManager.remove(entity);
        entityManager.flush();
    }

    @Override
    public E save(E entity) {
        entityManager.persist(entity);
        return entity;

    }

    @Override
    public List<E> findAll() {
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(entityClass);
        var entity = criteria.from(entityClass);

        return entityManager.createQuery(criteria.select(entity))
                .setHint(QueryHints.FETCH_SIZE, 100)
                .getResultList();
    }

}