package lk.nibm.userservice.service;

import lk.nibm.userservice.model.Role;
import lk.nibm.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing role-related operations.
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    @Autowired
    private final RoleRepository roleRepository;

    /**
     * Finds a role by its name.
     *
     * @param name The name of the role to find.
     * @return The found role or null if not found.
     */
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Creates a new role or updates an existing role with the given name.
     *
     * @param roleName The name of the role to create or update.
     * @return The created or updated role.
     */
    public Role createOrUpdateRole(String roleName) {
        Role existingRole = roleRepository.findByName(roleName);
        if (existingRole == null) {
            Role newRole = new Role();
            newRole.setName(roleName);
            return roleRepository.save(newRole);
        }
        return existingRole;
    }

    // Additional role-related business methods can be added here
}
