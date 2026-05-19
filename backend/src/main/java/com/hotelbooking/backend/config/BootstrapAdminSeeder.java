package com.hotelbooking.backend.config;

import com.hotelbooking.backend.domain.entities.StaffEntity;
import com.hotelbooking.backend.repositories.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapAdminSeeder implements ApplicationRunner {

    private final BootstrapAdminConfig config;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (!config.enabled()) {
            return;
        }

        if (config.email() == null || config.email().isBlank()) {
            log.warn("Bootstrap admin enabled, but app.bootstrap-admin.email is missing. Skipping.");
            return;
        }
        if (config.password() == null || config.password().isBlank()) {
            log.warn("Bootstrap admin enabled, but app.bootstrap-admin.password is missing. Skipping.");
            return;
        }

        if (staffRepository.findByEmail(config.email()).isPresent()) {
            return;
        }

        StaffEntity admin = StaffEntity.builder()
                .hotel(null)
                .email(config.email())
                .passwordHash(passwordEncoder.encode(config.password()))
                .role(2)
                .accountStatus(1)
                .build();

        staffRepository.save(admin);
        log.warn("Bootstrapped global ADMIN staff with email={} (disable via app.bootstrap-admin.enabled=false)", config.email());
    }
}



