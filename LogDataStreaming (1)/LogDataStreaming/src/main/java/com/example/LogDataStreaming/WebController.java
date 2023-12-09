package com.example.LogDataStreaming;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class WebController {

    @Autowired
    private MessageStorage storage;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("messages", storage.getMessages());
        return "index"; // Name of the Thymeleaf template
    }
}