package com.example.attempt2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    // Handle GET requests to /home
    @GetMapping("/home")
    public String home(Model model) {
        return "index";  // returns the index view
    }

    @GetMapping("/redemptionform")
    public String redemptionform(Model model) {
        return "redemptionform";  // returns the index view
    }

    @GetMapping("/giftingform")
    public String giftingform(Model model) {
        return "giftingform";  // returns the index view
    }

}
