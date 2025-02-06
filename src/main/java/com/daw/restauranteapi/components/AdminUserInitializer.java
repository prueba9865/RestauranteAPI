package com.daw.restauranteapi.components;

import com.daw.restauranteapi.entities.UserEntity;
import com.daw.restauranteapi.repositories.UserEntityRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminUserInitializer {

    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserInitializer(UserEntityRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (!userRepository.existsByUsername("admin")) {
            UserEntity admin = UserEntity.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .email("admin@admin.com")
                    .authorities(List.of("ROLE_ADMIN", "ROLE_USER"))
                    .build();
            userRepository.save(admin);
        }
    }
}
