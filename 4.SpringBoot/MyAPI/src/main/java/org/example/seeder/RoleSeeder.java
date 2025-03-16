package org.example.seeder;

import lombok.AllArgsConstructor;
import org.example.entities.RoleEntity;
import org.example.repository.IRoleRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@AllArgsConstructor
public class RoleSeeder {
    private final IRoleRepository roleRepository;

    public void seed() {
        if (roleRepository.count() > 0) return;

        var admin = new RoleEntity(null, "ADMIN");
        var user = new RoleEntity(null, "USER");
        roleRepository.saveAll(List.of(admin, user));
        System.out.println("---Roles seeded---");
    }
}
