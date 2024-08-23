package lk.nibm.userservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a user in the system.
 * Each user has a unique email, a name, a password, and a role.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    /**
     * Role assigned to the user.
     * The role is stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

    /**
     * Represents the roles a user can have.
     */
    public enum RoleEnum {
        USER,
        EVENT_ORG
    }
}
