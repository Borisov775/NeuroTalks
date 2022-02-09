package com.example.app.controllers;


import com.example.app.models.UserEntity;
import com.example.app.repo.UserRepo;
import com.example.app.servlets.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/reg")
public class RegController {
    @Autowired
    WebSecurityConfig webSecurityConfig;
    final private UserRepo userRepo;





    public RegController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;

    }

    @GetMapping
    public String getReg() {
        return "reg-page";
    }


    @PostMapping
    public String postReg(String firstName, String lastName, String repeat,String email,String researchArea,String country, String sex, String password, Map<String, Object> model) {
        final String status="user";
        if (!password.equals(repeat)) {
            model.put("message", "Password is bad");
            return "reg-page";
        }

        final UserEntity userWithSameName = userRepo.findByEmail(email);
        if (userWithSameName != null) {
            model.put("message", "Such user already exists");
            return "reg-page";
        }

        String encoded=webSecurityConfig.passwordEncoder().encode(password);
        //System.out.println(encoded);
        userRepo.save(new UserEntity(firstName,lastName,email,researchArea,country,sex,status,encoded));


        return "redirect:login";
    }


}