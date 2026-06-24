package com.walid.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CountriesController {

    private final WebClient webClient;

    @GetMapping("/countries")
    public Mono<?> getCountries() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(ctx -> {
                    JwtAuthenticationToken auth = (JwtAuthenticationToken) ctx.getAuthentication();
                    String token = auth.getToken().getTokenValue();
                    return webClient.get()
                            .uri("http://country-city-service:8081/countries")
                            .header("Authorization", "Bearer " + token)
                            .retrieve()
                            .bodyToMono(Object.class);
                });
    }
}
