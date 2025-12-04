package com.example.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirectController {

    @GetMapping({"/swagger-ui", "/swagger-ui/", "/swagger-ui/index.html"})
    public String redirectToSwaggerUi() {
        // springdoc serves UI at /swagger-ui.html (configured in application.properties)
        return "redirect:/swagger-ui.html";
    }
}
