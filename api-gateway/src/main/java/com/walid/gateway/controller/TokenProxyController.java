package com.walid.gateway.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class TokenProxyController {

    @Autowired
    private WebClient webClient;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/token")
    public Mono<ResponseEntity<String>> getToken(
            @RequestParam(required = false) String grant_type,
            @RequestParam(required = false) String client_id,
            @RequestParam(required = false) String client_secret,
            @RequestParam(required = false) String scope,
            @RequestHeader(name = HttpHeaders.CONTENT_TYPE, required = false) String contentType,
            @RequestBody(required = false) String body) {
        
        return Mono.fromCallable(() -> {
            String finalGrantType = grant_type;
            String finalClientId = client_id;
            String finalClientSecret = client_secret;
            String finalScope = scope;
            
            if (body != null && !body.isEmpty() && !body.equals("{}")) {
                try {
                    if (body.trim().startsWith("{")) {
                        JsonNode node = objectMapper.readTree(body);
                        if (node.has("grant_type")) finalGrantType = node.get("grant_type").asText();
                        if (node.has("client_id")) finalClientId = node.get("client_id").asText();
                        if (node.has("client_secret")) finalClientSecret = node.get("client_secret").asText();
                        if (node.has("scope")) finalScope = node.get("scope").asText();
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing body: " + e.getMessage());
                }
            }
            
            return buildTokenRequest(finalGrantType, finalClientId, finalClientSecret, finalScope);
        }).flatMap(m -> m);
    }
    
    private Mono<ResponseEntity<String>> buildTokenRequest(String grant_type, String client_id, String client_secret, String scope) {
        try {
            StringBuilder body = new StringBuilder();
            
            if (grant_type != null && !grant_type.isEmpty()) {
                body.append("grant_type=").append(URLEncoder.encode(grant_type, StandardCharsets.UTF_8.name()));
            } else {
                body.append("grant_type=client_credentials");
            }
            
            if (client_id != null && !client_id.isEmpty()) {
                if (body.length() > 0) body.append("&");
                body.append("client_id=").append(URLEncoder.encode(client_id, StandardCharsets.UTF_8.name()));
            }
            
            if (client_secret != null && !client_secret.isEmpty()) {
                if (body.length() > 0) body.append("&");
                body.append("client_secret=").append(URLEncoder.encode(client_secret, StandardCharsets.UTF_8.name()));
            }
            
            if (scope != null && !scope.isEmpty()) {
                if (body.length() > 0) body.append("&");
                body.append("scope=").append(URLEncoder.encode(scope, StandardCharsets.UTF_8.name()));
            }

            return webClient.post()
                    .uri("http://keycloak:8080/realms/country-system/protocol/openid-connect/token")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .bodyValue(body.toString())
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), 
                        response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody))))
                    .bodyToMono(String.class)
                    .map(response -> ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                            .body(response))
                    .onErrorResume(e -> Mono.just(ResponseEntity.status(400)
                            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                            .body("{\"error\": \"" + e.getMessage() + "\"}")));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(400)
                    .body("{\"error\": \"" + e.getMessage() + "\"}"));
        }
    }
}
