package com.example.app.controllers;

import com.example.app.models.Post;
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
public class BlogController {

    @Autowired
    private PostRepository repository;


    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Post> posts=repository.findAll();
        model.addAttribute("posts",posts);
        return "blog-main";
    }
    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        return "blog-add";
    }
    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title,@RequestParam String anons,@RequestParam String full_text, Model model){
        Post post=new Post(title,anons,full_text);
        repository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value="id") long id,Model model){
        Optional<Post> post=repository.findById(id);
        ArrayList<Post> result=new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post",result);
        return "blog-details";
    }
    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value="id") long id,Model model){
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
    public String blogPostUpdate(@PathVariable(value="id") long id, @RequestParam String title,@RequestParam String anons,@RequestParam String full_text, Model model){
        Post post=repository.findById(id).orElseThrow();
       //post.setTitle(title);
       // post.setAnnons(anons);
        post.setFull_text(full_text);
        repository.save(post);
        return "redirect:/blog";
    }
    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value="id") long id, Model model){
        Post post=repository.findById(id).orElseThrow();
        repository.delete(post);
        return "redirect:/blog";
    }
    @PostMapping("/blog/login")
    public String blogLogin(Model model){
        return "reg-page";
    }
}
