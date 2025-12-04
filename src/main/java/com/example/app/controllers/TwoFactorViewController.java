package com.example.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TwoFactorViewController {

    @GetMapping("/2fa")
    public String twoFactorPage() {
        return "2fa";
    }
}
