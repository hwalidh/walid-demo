package com.walid.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private WebClient webClient;

    public HealthController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/health/all")
    public Mono<Map<String, Object>> health() {

        return webClient.get()
                .uri("http://country-city-service:8081/actuator/health")
                .retrieve()
                .bodyToMono(Map.class)
                .map(sh -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("gateway", "UP");
                    result.put("country-city-service", sh);
                    return result;
                })
                .onErrorReturn(Map.of("gateway", "UP", "country-city-service", "DOWN"));
    }
}