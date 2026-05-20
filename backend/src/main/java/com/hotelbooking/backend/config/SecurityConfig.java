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
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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


    private static AppRole mapStaffRole(Integer staffRole) {
        if (staffRole != null && staffRole == 2) {
            return AppRole.ADMIN;
        }
        return AppRole.STAFF;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register/staff").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login/staff").permitAll()
                        .requestMatchers(HttpMethod.POST, "/bookings/availability").permitAll()
                        .requestMatchers(HttpMethod.POST, "/bookings/*/check-in").authenticated()
                        .requestMatchers(HttpMethod.POST, "/bookings/*/check-out").authenticated()
                        .requestMatchers(HttpMethod.GET, "/room-types").authenticated()
                        .requestMatchers(HttpMethod.GET, "/hotels").authenticated()
                        .requestMatchers(HttpMethod.POST, "/hotels").authenticated()
                        .requestMatchers(HttpMethod.POST, "/hotels/*/room-types").authenticated()
                        .requestMatchers(HttpMethod.POST, "/room-types/*/rooms").authenticated()
                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:5174"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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