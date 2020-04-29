package com.league.controller;


import com.league.model.User;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
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

    @GetMapping("search")
    public String search(@RequestParam String phrase) {

        System.out.println(phrase);
        //userService.search(phrase);

        return "redirect:/user/index";
    }
}
