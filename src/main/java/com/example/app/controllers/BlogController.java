package com.example.app.controllers;

import com.example.app.models.Post;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.app.repo.PostRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@Service
@Api(value = "Blog", description = "Blog management endpoints")
public class BlogController {

    @Autowired
    private PostRepository repository;


    @GetMapping("/blog")
    @ApiOperation(value = "Get All Blog Posts", notes = "Retrieves all blog posts")
    @ApiResponse(code = 200, message = "Success - returns blog-main view")
    public String blogMain(Model model){
        Iterable<Post> posts=repository.findAll();
        model.addAttribute("posts",posts);
        return "blog-main";
    }
    @GetMapping("/blog/add")
    @ApiOperation(value = "Get Add Blog Form", notes = "Returns the form to add a new blog post")
    @ApiResponse(code = 200, message = "Success - returns blog-add view")
    public String blogAdd(Model model){
        return "blog-add";
    }
    @PostMapping("/blog/add")
    @ApiOperation(value = "Create Blog Post", notes = "Creates a new blog post")
    @ApiResponse(code = 200, message = "Success - redirects to /blog")
    public String blogPostAdd(@ApiParam(value = "Post title", required = true) @RequestParam String title,@ApiParam(value = "Post synopsis", required = true) @RequestParam String anons,@ApiParam(value = "Full post text", required = true) @RequestParam String full_text, Model model){
        Post post=new Post(title,anons,full_text);
        repository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    @ApiOperation(value = "Get Blog Post Details", notes = "Retrieves details of a specific blog post")
    @ApiResponse(code = 200, message = "Success - returns blog-details view")
    public String blogDetails(@ApiParam(value = "Post ID", required = true) @PathVariable(value="id") long id,Model model){
        Optional<Post> post=repository.findById(id);
        ArrayList<Post> result=new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post",result);
        return "blog-details";
    }
    @GetMapping("/blog/{id}/edit")
    @ApiOperation(value = "Get Blog Edit Form", notes = "Returns form to edit a blog post")
    @ApiResponse(code = 200, message = "Success - returns blog-edit view")
    public String blogEdit(@ApiParam(value = "Post ID", required = true) @PathVariable(value="id") long id,Model model){
        if(!repository.existsById(id)){
            return "redirect:/blog";
        }
        Optional<Post> post=repository.findById(id);
        ArrayList<Post> result=new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post",result);
        return "blog-edit";
    }
    @PostMapping("/blog/{id}/edit")
    @ApiOperation(value = "Update Blog Post", notes = "Updates an existing blog post")
    @ApiResponse(code = 200, message = "Success - redirects to /blog")
    public String blogPostUpdate(@ApiParam(value = "Post ID", required = true) @PathVariable(value="id") long id, @ApiParam(value = "Title") @RequestParam String title,@ApiParam(value = "Synopsis") @RequestParam String anons,@ApiParam(value = "Full text") @RequestParam String full_text, Model model){
        Post post=repository.findById(id).orElseThrow();
       //post.setTitle(title);
       // post.setAnnons(anons);
        post.setFull_text(full_text);
        repository.save(post);
        return "redirect:/blog";
    }
    @PostMapping("/blog/{id}/remove")
    @ApiOperation(value = "Delete Blog Post", notes = "Deletes a blog post by ID")
    @ApiResponse(code = 200, message = "Success - redirects to /blog")
    public String blogPostDelete(@ApiParam(value = "Post ID", required = true) @PathVariable(value="id") long id, Model model){
        Post post=repository.findById(id).orElseThrow();
        repository.delete(post);
        return "redirect:/blog";
    }
    @PostMapping("/blog/login")
    @ApiOperation(value = "Blog Login Redirect", notes = "Redirects to registration page")
    @ApiResponse(code = 200, message = "Success - returns reg-page view")
    public String blogLogin(Model model){
        return "reg-page";
    }
}
