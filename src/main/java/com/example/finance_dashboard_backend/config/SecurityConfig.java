package com.example.finance_dashboard_backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Logger for security events
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Configure HTTP security, roles, and JWT filter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (Swagger and Auth)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        // Role-based access control
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/records/**").hasAnyRole("ADMIN", "ANALYST")
                        .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "ANALYST", "VIEWER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        // Handle unauthorized access
                        .authenticationEntryPoint((request, response, authException) -> {
                            logger.warn("Unauthorized access attempt: {}", authException.getMessage());
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Login required\"}");
                        })
                        // Handle forbidden access
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            logger.warn("Access denied: {}", accessDeniedException.getMessage());
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"Access denied\"}");
                        })
                );

        // Add JWT filter before username/password authentication
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * In-memory users for testing purposes
     */
    @Bean
    public UserDetailsService userDetailsService() {
        logger.info("Initializing in-memory users for SecurityConfig");
        UserDetails admin = User.withUsername("admin").password("1234").roles("ADMIN").build();
        UserDetails analyst = User.withUsername("analyst").password("1234").roles("ANALYST").build();
        UserDetails viewer = User.withUsername("viewer").password("1234").roles("VIEWER").build();
        return new InMemoryUserDetailsManager(admin, analyst, viewer);
    }

    /**
     * Password encoder (NoOp for testing only)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Using NoOpPasswordEncoder (for testing only)");
        return NoOpPasswordEncoder.getInstance(); // For testing only
    }

    /**
     * Configure authentication manager using DaoAuthenticationProvider
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        logger.info("AuthenticationManager configured with DaoAuthenticationProvider");
        return new ProviderManager(authProvider);
    }
}