package org.example.carrental.config;

import org.example.carrental.model.User;
import org.example.carrental.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("ad@min.com") == null) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("ad@min.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole("ADMIN");
                admin.setNumeroCompte("CPT-001");
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("cli@ent.com") == null) {
                User client = new User();
                client.setName("Client");
                client.setEmail("cli@ent.com");
                client.setPassword(passwordEncoder.encode("client"));
                client.setRole("CLIENT");
                client.setNumeroCompte("CPT-002");
                userRepository.save(client);
            }
        };
    }
}
