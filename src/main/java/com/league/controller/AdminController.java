package com.league.controller;

import com.league.model.User;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("index")
    public String index(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "admin/index";
    }

    @GetMapping("editUser")
    public String editUser(@RequestParam("userId") int userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute(user);
        return "admin/editUser";
    }

    @PostMapping("editUserProceed")
    public String editPlayerProceed(@ModelAttribute("user") User user) {
        userService.actualizeUser(user);
        return "redirect:/admin/index";
    }

    @GetMapping("deleteUser")
    public String deleteUser(@RequestParam("userId") int userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/index";
    }
}
