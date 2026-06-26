package com.walid.demo.country_city_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // On dit à Spring Security de laisser passer Swagger sans token
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/actuator/prometheus",
                    "/actuator/health",
                    "/actuator/metrics"
                ).permitAll()
                // Tout le reste (vos contrôleurs pays/villes) reste sécurisé
                .anyRequest().authenticated()
            )
            // On indique qu'on attend un jeton JWT pour les autres requêtes
            .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }
}