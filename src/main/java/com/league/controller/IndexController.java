package com.league.controller;


import com.league.model.User;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {

    @Autowired
    UserService userService;

    @GetMapping("index")
    public String index() { return "index";
    }

    @GetMapping("registration")
    public String registration(Model model) {

        User user = new User();
        model.addAttribute("user", user);

        return "registration";
    }

    @PostMapping("registrationProceed")
    public String registrationProceed(@ModelAttribute("user") User user) {

        userService.addUser(user);

        return "redirect:/index";
    }
}
