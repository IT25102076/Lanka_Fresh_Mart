package com.lankafreshmart.lanka_fresh_mart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public pages — anyone can access
                .requestMatchers("/", "/home", "/register", "/login", "/request-reset-otp", "/reset-password", "/css/**", "/js/**", "/images/**", "/uploads/**", "/privacy", "/terms", "/products", "/api/search").permitAll()
                // fast-switch is a dev-only tool
                .requestMatchers("/fast-switch").authenticated()
                // Role-based access
                .requestMatchers("/products/manage/**").hasAnyAuthority("STORE_SUPERVISOR", "OPERATIONS_MANAGER")
                .requestMatchers("/inventory/**").hasAnyAuthority("STORE_SUPERVISOR", "OPERATIONS_MANAGER")
                .requestMatchers("/orders/manage/**").hasAnyAuthority("CUSTOMER_RELATIONS_OFFICER", "OPERATIONS_MANAGER")
                .requestMatchers("/deliveries/manage/**", "/deliveries/update/**").hasAnyAuthority("DELIVERY_COORDINATOR", "OPERATIONS_MANAGER")
                .requestMatchers("/deliveries/track").hasAuthority("CUSTOMER")
                .requestMatchers("/drivers/**").hasAnyAuthority("DELIVERY_COORDINATOR", "OPERATIONS_MANAGER")
                .requestMatchers("/routing/**").hasAnyAuthority("DELIVERY_COORDINATOR", "OPERATIONS_MANAGER")
                .requestMatchers("/finance/**").hasAnyAuthority("FINANCE_EXECUTIVE", "OPERATIONS_MANAGER")
                .requestMatchers("/payments/**").hasAnyAuthority("FINANCE_EXECUTIVE", "OPERATIONS_MANAGER")
                .requestMatchers("/dashboard/**", "/executive-dashboard").hasAuthority("OPERATIONS_MANAGER")
                .requestMatchers("/support/manage/**").hasAnyAuthority("CUSTOMER_RELATIONS_OFFICER", "OPERATIONS_MANAGER")
                .requestMatchers("/cart/**", "/orders/my-orders/**", "/checkout/**", "/support/my-tickets", "/support/create").hasAuthority("CUSTOMER")
                // Everything else requires login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/home?error=access_denied");
                })
            );

        return http.build();
    }
}
