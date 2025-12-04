package com.example.app.controllers;


import com.example.app.models.UserEntity;
import com.example.app.repo.UserRepo;
import com.example.app.servlets.WebSecurityConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/reg")
@Api(value = "Registration", description = "User registration endpoints")
public class RegController {
    @Autowired
    WebSecurityConfig webSecurityConfig;
    final private UserRepo userRepo;





    public RegController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;

    }

    @GetMapping
    @ApiOperation(value = "Get Registration Page", notes = "Returns the registration form")
    @ApiResponse(code = 200, message = "Success - returns reg-page view")
    public String getReg() {
        return "reg-page";
    }


    @PostMapping
    @ApiOperation(value = "Register User", notes = "Creates a new user account with provided credentials")
    @ApiResponse(code = 200, message = "Registration successful - redirects to login")
    public String postReg(
            @ApiParam(value = "First name", required = true) String firstName,
            @ApiParam(value = "Last name", required = true) String lastName,
            @ApiParam(value = "Password confirmation (must match password)", required = true) String repeat,
            @ApiParam(value = "Email address (unique)", required = true) String email,
            @ApiParam(value = "Research area", required = true) String researchArea,
            @ApiParam(value = "Country", required = true) String country,
            @ApiParam(value = "Sex/Gender", required = true) String sex,
            @ApiParam(value = "Password", required = true) String password,
            Map<String, Object> model) {
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