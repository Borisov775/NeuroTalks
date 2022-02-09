package com.example.app.controllers;

import com.example.app.models.Post;
import com.example.app.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class PostDetails {
    @Autowired
    private PostRepository repository;
    @GetMapping("/homepage/post/{id}")
    public String postDetails(@PathVariable(value = "id") long id, Model model){
        Optional<Post> post=repository.findById(Long.valueOf(id));
        ArrayList<Post> result=new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post",result.get(0));
        return "post-details";
    }
}
