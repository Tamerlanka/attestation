package com.example.springsecurityapp.controllers;

import com.example.springsecurityapp.models.Product;
import com.example.springsecurityapp.repositories.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {


    @GetMapping("/authentication")
    public String login(){
        return "authentication";
    }


}
