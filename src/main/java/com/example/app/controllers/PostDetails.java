package com.example.app.controllers;

import com.example.app.models.Post;
import com.example.app.repo.PostRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@Api(value = "Post Details", description = "Post details endpoints")
public class PostDetails {
    @Autowired
    private PostRepository repository;
    @GetMapping("/homepage/post/{id}")
    @ApiOperation(value = "Get Post Details", notes = "Retrieves details of a specific homepage post by ID")
    @ApiResponse(code = 200, message = "Success - returns post-details view")
    public String postDetails(@ApiParam(value = "Post ID", required = true) @PathVariable(value = "id") long id, Model model){
        Optional<Post> post=repository.findById(Long.valueOf(id));
        ArrayList<Post> result=new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post",result.get(0));
        return "post-details";
    }
}
