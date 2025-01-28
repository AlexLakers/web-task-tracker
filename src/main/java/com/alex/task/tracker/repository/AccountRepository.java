package com.alex.task.tracker.repository;

import com.alex.task.tracker.entity.Account;
import com.alex.task.tracker.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class is account repository.
 * It contains all the 'CRUD' functions realized into {@link BaseRepository baseRepository} and additional func.
 * This repository interaction with 'account' table in the database.
 *
 * @see Account entity.
 */

public class AccountRepository extends BaseRepository<Long, Account> {
    private static final AccountRepository INSTANCE = new AccountRepository(HibernateUtil.getSessionProxy());

    public static AccountRepository getInstance() {
        return INSTANCE;
    }

    public AccountRepository(EntityManager entityManager) {
        super(Account.class, entityManager);
    }

    public Optional<Account> findById(Long id, Map<String, Object> properties) {
        return Optional.ofNullable(getEntityManager().find(getEntityClass(), id, properties));
    }

    /**
     * Returns account is wrapped in {@link Optional optional} for null safe.
     * The search of a specific account in the database occurs by login and pass.
     *
     * @param login    account login
     * @param password account pass.
     * @return account is wrapped in {@link Optional optional}.
     * If transmitted login and pass exist then returns not empty wrapped account.
     * Else-empty wrapped account.
     */
    public Optional<Account> findByLoginAndPassword(String login, String password) {

        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Account.class);
        var account = criteria.from(Account.class);

        //Selections
       /* List<Selection<?>> selections = new ArrayList<>();
        selections.add(account.get(Account_.ID));
        selections.add(account.get(Account_.LOGIN));
        selections.add(cb.concat(
                cb.concat(
                        account.get(Account_.ACCOUNT_INFO).get(AccountInfo_.FIRST_NAME), " "),
                account.get(Account_.ACCOUNT_INFO).get(AccountInfo_.LAST_NAME))
        );
        selections.add(account.get(Account_.roles));*/

        //Predicates
        List<Predicate> predicates = new ArrayList<>();
      // predicates.add(cb.equal(account.get(Account_.LOGIN), login));
     //  predicates.add(cb.equal(account.get(Account_.PASSWORD), password));
        criteria.select(account)
                /*cb.construct(ReadAccountDto.class, selections.toArray(Selection[]::new)*/

                .where(
                        predicates.toArray(Predicate[]::new)
                );

        return (getEntityManager().createQuery(criteria)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst()
        );

    }
}