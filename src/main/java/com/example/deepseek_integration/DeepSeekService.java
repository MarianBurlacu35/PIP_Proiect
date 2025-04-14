package com.example.deepseek_integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeepSeekService {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    @Value("${deepseek.api.model}")
    private String model;

    @Value("${deepseek.api.temperature}")
    private double temperature;

    private final RestTemplate restTemplate;

    @Autowired
    public DeepSeekService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String askDeepSeek(String prompt) {
        // Crearea structurii JSON pentru cererea API
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);  // setăm modelul (ex: deepseek-chat)
        
        // Creăm lista de mesaje, de exemplu mesajul de la utilizator
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt); // mesajul de intrare
        
        // Adăugăm mesajele într-o listă
        requestBody.put("messages", new Object[]{message});

        // Setările header-ului cu cheia API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Crearea entității care conține body-ul cererii și header-ele
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Trimiterea cererii POST către DeepSeek
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

        // Extragem răspunsul
        Map<String, Object> choices = (Map<String, Object>) ((Map<String, Object>) response.getBody()).get("choices");
        Map<String, Object> messageResponse = (Map<String, Object>) ((Map<String, Object>) choices.get(0)).get("message");

        return (String) messageResponse.get("content");
    }
}
