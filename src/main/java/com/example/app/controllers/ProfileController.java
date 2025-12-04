package com.example.app.controllers;

import com.example.app.models.Post;
import com.example.app.models.Profile;
import com.example.app.models.UserEntity;
import com.example.app.repo.PostRepository;
import com.example.app.repo.ProfileRepository;
import com.example.app.repo.UserRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@Api(value = "Profile", description = "User profile endpoints")
public class ProfileController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    PostRepository postRepository;
    @GetMapping("/homepage/{email}")
    @ApiOperation(value = "Get User Profile", notes = "Retrieves a user's profile page by email")
    @ApiResponse(code = 200, message = "Success - returns profile-page view")
    public String getProfilePage(@ApiParam(value = "User email address", required = true) @PathVariable(value = "email") String email, Model model){
        UserEntity userEntity= userRepo.findByEmail(email);
        long publicationCount = postRepository.countByEmail(email);
        model.addAttribute("user",userEntity);
        model.addAttribute("publicationCount", publicationCount);
        return "profile-page";
    }
}
