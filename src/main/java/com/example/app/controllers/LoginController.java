package com.example.app.controllers;

import com.example.app.models.UserEntity;
import com.example.app.repo.UserRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@Api(value = "Authentication", description = "Login endpoints")
public class LoginController {
    final private UserRepo userRepo;

    public LoginController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/login")
    @ApiOperation(value = "Get Login Page", notes = "Returns the login form")
    @ApiResponse(code = 200, message = "Success - returns login view")
    public String login() {
        return "login";
    }



    /*@PostMapping("/login")
    public String postLogin(@RequestParam String email,@RequestParam String password, Map<String, Object> model) {
        final UserEntity registeredUser = userRepo.findByEmail(email);
        if (registeredUser == null) {
            model.put("message", "Such account doesn't exist");
            return "login";
        }
        //if (!registeredUser.getPassword().equals(password)) {
        //    model.put("message","Incorrect password");
        //}
        return "redirect:home-page";
    }

     */
}
