package com.alex.task.tracker.repository;

import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.entity.Role_;
import com.alex.task.tracker.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.graph.GraphSemantic;

import java.util.List;
import java.util.Optional;

/**
 * This class is role repository.
 * It contains all the 'CRUD' functions realized into {@link BaseRepository baseRepository} and additional func.
 * This repository interaction with 'role' table in the database.
 *
 * @see Role entity.
 */
public class RoleRepository extends BaseRepository<Integer, Role> {
    private static final RoleRepository INSTANCE = new RoleRepository(HibernateUtil.getSessionProxy());

    public static RoleRepository getInstance() {
        return INSTANCE;
    }

    public RoleRepository(EntityManager entityManager) {
        super(Role.class, entityManager);
    }

    /**
     * Returns role is wrapped in {@link Optional optional} for null safe.
     * The search of a specific role in the database occurs by role name.
     *
     * @param name role name.
     * @return If transmitted role name exists then returns not empty wrapped role.
     * Else-empty wrapped role.
     */
    public Optional<Role> findByName(String name) {
    /*    return Optional.ofNullable(getEntityManager().createQuery("select r from Role r where name=:name",Role.class)
                .setParameter("name",name)
                .getSingleResult());
*/
        //   try {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Role.class);
        var role = criteria.from(Role.class);
    criteria.select(role).where(cb.equal(role.get(Role_.NAME), name));

        return getEntityManager().createQuery(criteria)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), getEntityManager().getEntityGraph("Role.permissions"))
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }

    /**
     * Returns all the available persistent entities by list of role.
     *
     * @param roles list of role.
     * @return persistent entities.
     * @return list of persistent role.
     * If transmitted roles are unavailable then list will be empty.
     * Else-not empty list.
     */
    public List<Role> findAll(List<String> roles) {
        return getEntityManager().createQuery("select r from Role r where r.name in (:roles)", Role.class)
                .setParameter("roles", roles)
                .getResultList();
    }
}
     /*  }
        catch (NoResultException e) {
            throw new RepositoryException("The  role %1$s is not found in the database".formatted(name),e);
        }*/


