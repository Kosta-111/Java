package org.example.seeder;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private CategorySeeder categorySeeder;
    private ProductSeeder productSeeder;
    private RoleSeeder roleSeeder;
    private UserSeeder userSeeder;

    @Override
    public void run(String... args) throws Exception {
        categorySeeder.seed();
        productSeeder.seed();
        roleSeeder.seed();
        userSeeder.seed();
    }
}