package com.example.app.controllers;

import com.example.app.auth.UserEntityDetailsService;
import com.example.app.models.Post;
import com.example.app.models.UserEntity;
import com.example.app.repo.PostRepository;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@Service
public class HomeController {
    @Autowired
    private PostRepository repository;
    @Autowired
    UserRepo userRepo;


    @GetMapping("/homepage")
    public String homepageMakePostsAlive(Model model){
        Iterable<Post> posts=repository.findAll();


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        UserEntity userEntity = userRepo.findByEmail(username);
        String email=userEntity.getEmail();


        model.addAttribute("email",email);
        model.addAttribute("posts",posts);
        return "home-page";
    }


    @PostMapping("/homepage")
    public String login(){
            return "home-page";
        }

}
