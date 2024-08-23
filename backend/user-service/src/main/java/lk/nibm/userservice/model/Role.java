package lk.nibm.userservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a role that can be assigned to a user.
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Uncomment the following fields and methods if a bidirectional relationship with User is needed.
    // @ManyToMany(mappedBy = "roles")
    // private Set<User> users;

    // Uncomment this constructor if you need to initialize Role with both name and users.
    // public Role(String name, Set<User> users) {
    //     this.name = name;
    //     this.users = users;
    // }
}
