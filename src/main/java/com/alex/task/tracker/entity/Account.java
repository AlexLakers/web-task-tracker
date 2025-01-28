package com.alex.task.tracker.entity;

import com.alex.task.tracker.converter.PasswordAttributeConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SortNatural;

import java.time.LocalDate;
import java.util.*;

/**
 * This is an entity class.
 * It associated with 'account' table in the database.
 *
 * @see AuditEntity auditing entity.
 * @see AccountInfo embedded part of account.
 * @see Role role.
 * @see Task task.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "login")
@ToString(exclude = {"performerTasks", "customerTasks", "role"})
@Builder
@Entity
@Table(name = "account")
/*@NamedEntityGraph(name="withPerformerTasks",
attributeNodes = @NamedAttributeNode("performerTasks",))*/

public class Account extends AuditEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    private LocalDate birthday;

    @Convert(converter = PasswordAttributeConverter.class)

    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false))
    })
    private AccountInfo accountInfo;

    /*    @ManyToOne(optional = false,fetch=FetchType.LAZY)
        @JoinColumn(name="role_id",nullable = false)
        private Role role;*/
    @ManyToMany
    @Builder.Default
    @JoinTable(name = "account_role",
            /* schema = "task_manager_storage",*/
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))

    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "performer", fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<Task> performerTasks = new ArrayList<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @SortNatural
    private List<Task> customerTasks = new ArrayList<>();

    public void addRole(Role role) {
        role.getAccounts().add(this);
        roles.add(role);
    }

    public void removeRole(Role role) {
        role.getAccounts().remove(this);
        roles.remove(role);
    }

    public void addPerformerTask(Task task) {
        task.setPerformer(this);
        performerTasks.add(task);
    }

    public void removePerformerTask(Task task) {
        task.setPerformer(null);
        performerTasks.remove(task);
    }

    public void addCustomerTask(Task task) {
        task.setCustomer(this);
        customerTasks.add(task);
    }

    public void removeCustomerTask(Task task) {
        task.setCustomer(null);
        customerTasks.remove(task);
    }


}
