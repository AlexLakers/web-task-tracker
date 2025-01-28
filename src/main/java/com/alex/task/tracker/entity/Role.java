package com.alex.task.tracker.entity;

import com.alex.task.tracker.entity.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an entity class.
 * It associated with 'role' table in the database.
 *
 * @see Account account.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString(exclude = {"accounts", "permissions"})
@Entity
@Table(name = "role" /*schema = "task_manager_storage"*/)
@NamedEntityGraph(name = "Role.permissions",
        attributeNodes = {@NamedAttributeNode("permissions")})
public class Role extends AuditEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name;

    //? Maybe it is redundant
    @ManyToMany(mappedBy = "roles")
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();

    /*    @ManyToMany
        @JoinTable(name = "role_permission",
                schema = "task_manager_storage",
                joinColumns = @JoinColumn(name = "role_id"),
                inverseJoinColumns = @JoinColumn(name = "permission_id"))
        @Builder.Default
        private List<Permission> permissions = new ArrayList<>();
        public void addPermission(Permission permission) {
            permission.getRoles().add(this);
            permissions.add(permission);
        }
        public void removePermission(Permission permission) {
            permission.getRoles().remove(this);
            permissions.remove(permission);
        }*/
    @ElementCollection
    @CollectionTable(name = "role_permissions"/*, schema = "task_manager_storage"*/,
            joinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    @Column(name = "permission")
    private List<String> permissions = new ArrayList<>();

   /* public void add(String permission) {
        permissions.add(permission);
    }
    public void remove(String permission) {
        permissions.remove(permission);
    }*/

}
