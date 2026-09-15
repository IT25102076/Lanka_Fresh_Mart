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
                .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                // Role-based access
                .requestMatchers("/products/manage/**").hasAnyAuthority("STORE_SUPERVISOR", "OPERATIONS_MANAGER")
                .requestMatchers("/inventory/**").hasAnyAuthority("STORE_SUPERVISOR", "OPERATIONS_MANAGER")
                .requestMatchers("/orders/manage/**").hasAnyAuthority("CUSTOMER_RELATIONS_OFFICER", "OPERATIONS_MANAGER")
                .requestMatchers("/delivery/**").hasAnyAuthority("DELIVERY_COORDINATOR", "OPERATIONS_MANAGER")
                .requestMatchers("/payments/**").hasAnyAuthority("FINANCE_EXECUTIVE", "OPERATIONS_MANAGER")
                .requestMatchers("/dashboard/**").hasAuthority("OPERATIONS_MANAGER")
                .requestMatchers("/cart/**", "/orders/my/**").hasAuthority("CUSTOMER")
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
            );

        return http.build();
    }
}
