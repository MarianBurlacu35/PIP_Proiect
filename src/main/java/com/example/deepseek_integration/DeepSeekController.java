package com.example.deepseek_integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeepSeekController {

    @Autowired
    private DeepSeekService deepSeekService;

    @GetMapping("/ask")
    public String askDeepSeek(@RequestParam String prompt) {
        return deepSeekService.askDeepSeek(prompt);
    }
}