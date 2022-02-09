package com.example.app.controllers;

import com.example.app.models.Post;
import com.example.app.models.UserEntity;
import com.example.app.repo.PostRepository;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

@Controller
public class PostController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    private PostRepository repository;
    @GetMapping("/homepage/post")
    public String postadd(Model model){
        return "post-page";
    }
    @PostMapping("/homepage/post")
    public String postAdd(@RequestParam String topic, @RequestParam String full_text,@RequestParam String urlImage,@RequestParam String theme, Model model){


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        UserEntity userEntity =userRepo.findByEmail(username);
        Long profile_id=userEntity.getId();
        String NameOfUser=userEntity.getFirstName()+" "+userEntity.getLastName()+" ";
        int timeOfCreation_1=LocalDate.now().getDayOfMonth();
        int timeOfCreation_2=LocalDate.now().getYear();
        String shortDescription=full_text.substring(0,100)+"...";
        String timeOfCreation=(timeOfCreation_1)+" "+ String.valueOf(LocalDate.now().getMonth())+" "+timeOfCreation_2;
        Post post=new Post(topic,full_text,urlImage,NameOfUser,username+" ",timeOfCreation,theme,shortDescription,profile_id);
        repository.save(post);
        return "redirect:/homepage";
    }




}
