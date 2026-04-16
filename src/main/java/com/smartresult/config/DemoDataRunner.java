package com.smartresult.config;

import com.smartresult.model.Role;
import com.smartresult.model.User;
import com.smartresult.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoDataRunner {

  @Bean
  CommandLineRunner seedDemoUsers(UserRepository userRepository, PasswordEncoder encoder) {
    return args -> {
      seed(userRepository, encoder, "Aaru", "aaru@student.edu", "Password@123", Role.STUDENT);
      seed(userRepository, encoder, "Dr. Meera Iyer", "meera@faculty.edu", "Password@123", Role.FACULTY);
      seed(userRepository, encoder, "System Admin", "admin@srads.local", "Password@123", Role.ADMIN);
    };
  }

  private void seed(UserRepository repo, PasswordEncoder encoder, String name, String email, String password, Role role) {
    if (repo.existsByEmailIgnoreCase(email)) return;
    User u = new User();
    u.setName(name);
    u.setEmail(email);
    u.setRole(role);
    u.setPasswordHash(encoder.encode(password));
    repo.save(u);
  }
}

