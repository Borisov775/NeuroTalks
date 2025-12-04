package com.example.app.controllers;

import com.example.app.models.Post;
import com.example.app.repo.PostRepository;
import com.example.app.repo.UserRepo;
import com.example.app.models.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @Autowired
    private UserRepo userRepo;
    @GetMapping("/homepage/post/{id}")
    @ApiOperation(value = "Get Post Details", notes = "Retrieves details of a specific homepage post by ID")
    @ApiResponse(code = 200, message = "Success - returns post-details view")
    public String postDetails(@ApiParam(value = "Post ID", required = true) @PathVariable(value = "id") long id, Model model){
        Optional<Post> post=repository.findById(Long.valueOf(id));
        ArrayList<Post> result=new ArrayList<>();
        post.ifPresent(result::add);
        Post p = result.get(0);
        model.addAttribute("post", p);

        // expose whether current authenticated user is the author
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            username = principal.toString();
        }
        boolean isAuthor = false;
        if (username != null && p.getEmail() != null) {
            isAuthor = username.equalsIgnoreCase(p.getEmail().trim());
        }
        model.addAttribute("isAuthor", isAuthor);
        return "post-details";
    }

    @PostMapping("/homepage/post/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        Optional<Post> postOpt = repository.findById(id);
        if (!postOpt.isPresent()) {
            return "redirect:/homepage";
        }
        Post post = postOpt.get();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            username = principal.toString();
        }

        if (username != null && post.getEmail() != null && username.equalsIgnoreCase(post.getEmail().trim())) {
            repository.deleteById(id);
        }
        return "redirect:/homepage";
    }
}
