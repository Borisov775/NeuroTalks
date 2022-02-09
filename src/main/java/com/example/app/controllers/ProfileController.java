package com.example.app.controllers;

import com.example.app.models.Profile;
import com.example.app.models.UserEntity;
import com.example.app.repo.ProfileRepository;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
@Controller
public class ProfileController {
    @Autowired
    UserRepo userRepo;
    @GetMapping("/homepage/{email}")
    public String getProfilePage(@PathVariable(value = "email") String email, Model model){
        UserEntity userEntity= userRepo.findByEmail(email);
        model.addAttribute("user",userEntity);
        return "profile-page";
    }
}
