package com.hotelbooking.backend.config;


import com.hotelbooking.backend.domain.entities.StaffEntity;
import com.hotelbooking.backend.domain.entities.UserEntity;
import com.hotelbooking.backend.repositories.StaffRepository;
import com.hotelbooking.backend.repositories.UserRepository;
import com.hotelbooking.backend.security.AppRole;
import com.hotelbooking.backend.security.JwtAuthenticationFilter;
import com.hotelbooking.backend.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService) {
        return new JwtAuthenticationFilter(authenticationService);
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository, StaffRepository staffRepository) {
        return username -> {
            UserEntity user = userRepository.findByEmail(username).orElse(null);
            if (user != null) {
                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPasswordHash())
                        .authorities(List.of(new SimpleGrantedAuthority(user.getRole().asAuthority())))
                        .build();
            }

            StaffEntity staff = staffRepository.findByEmail(username).orElseThrow(
                    () -> new UsernameNotFoundException("User not found")
            );

            AppRole staffRole = mapStaffRole(staff.getRole());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(staff.getEmail())
                    .password(staff.getPasswordHash())
                    .authorities(List.of(new SimpleGrantedAuthority(staffRole.asAuthority())))
                    .build();
        };
    }

    /**
     * staff.role meaning in DB:
     * 1 = receptioner (STAFF)
     * 2 = manager (MANAGER)
     */
    private static AppRole mapStaffRole(Integer staffRole) {
        if (staffRole != null && staffRole == 2) {
            return AppRole.MANAGER;
        }
        return AppRole.STAFF;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/bookings/availability").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
