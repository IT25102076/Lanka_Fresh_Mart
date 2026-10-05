package com.lankafreshmart.lanka_fresh_mart.config;

import com.lankafreshmart.lanka_fresh_mart.model.User;
import com.lankafreshmart.lanka_fresh_mart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0 || !userRepository.existsByEmail("customer@test.com")) {
            System.out.println("==================================================");
            System.out.println("SEEDING DEFAULT TEST ACCOUNTS...");
            
            String password = passwordEncoder.encode("Password123");

            createUserIfNotFound("Customer", "User", "customer@test.com", password, User.Role.CUSTOMER);
            createUserIfNotFound("Supervisor", "User", "supervisor@test.com", password, User.Role.STORE_SUPERVISOR);
            createUserIfNotFound("Delivery", "User", "delivery@test.com", password, User.Role.DELIVERY_COORDINATOR);
            createUserIfNotFound("Finance", "User", "finance@test.com", password, User.Role.FINANCE_EXECUTIVE);
            createUserIfNotFound("Support", "User", "support@test.com", password, User.Role.CUSTOMER_RELATIONS_OFFICER);
            createUserIfNotFound("Operations", "User", "operations@test.com", password, User.Role.OPERATIONS_MANAGER);
            
            System.out.println("All test accounts created successfully!");
            System.out.println("Password for all accounts is: Password123");
            System.out.println("==================================================");
        }
    }

    private void createUserIfNotFound(String firstName, String lastName, String email, String password, User.Role role) {
        if (!userRepository.existsByEmail(email)) {
            User user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(password)
                    .phone("0712345678")
                    .address("LFM Headquarters")
                    .role(role)
                    .build();
            userRepository.save(user);
        }
    }


}
