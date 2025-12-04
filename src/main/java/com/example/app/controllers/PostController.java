package com.example.app.controllers;

import com.example.app.models.Post;
import com.example.app.models.UserEntity;
import com.example.app.repo.PostRepository;
import com.example.app.repo.UserRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
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
@Api(value = "Posts", description = "Homepage post management endpoints")
public class PostController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    private PostRepository repository;
    @GetMapping("/homepage/post")
    @ApiOperation(value = "Get Post Creation Form", notes = "Returns the form to create a new homepage post")
    @ApiResponse(code = 200, message = "Success - returns post-page view")
    public String postadd(Model model){
        return "post-page";
    }
    @PostMapping("/homepage/post")
    @ApiOperation(value = "Create Homepage Post", notes = "Creates a new post on the user's homepage")
    @ApiResponse(code = 200, message = "Success - redirects to /homepage")
    public String postAdd(
            @ApiParam(value = "Post topic/title", required = true) @RequestParam String topic,
            @ApiParam(value = "Full post text", required = true) @RequestParam String full_text,
            @ApiParam(value = "Image URL", required = true) @RequestParam String urlImage,
            @ApiParam(value = "Post theme/category", required = true) @RequestParam String theme,
            Model model){


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
        String shortDescription;
        if (full_text == null) {
            shortDescription = "";
        } else if (full_text.length() > 100) {
            shortDescription = full_text.substring(0, 100) + "...";
        } else {
            shortDescription = full_text;
        }
        String timeOfCreation=(timeOfCreation_1)+" "+ String.valueOf(LocalDate.now().getMonth())+" "+timeOfCreation_2;
        Post post=new Post(topic,full_text,urlImage,NameOfUser,username+" ",timeOfCreation,theme,shortDescription,profile_id);
        repository.save(post);
        return "redirect:/homepage";
    }




}
