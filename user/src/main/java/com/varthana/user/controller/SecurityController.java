package com.varthana.user.controller;

import com.varthana.user.entity.User;
import com.varthana.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {
    @Autowired
    private UserService userService;
    @GetMapping("/showLoginPage")
    public String showLoginPage() {
        return "login";
    }

    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/register")
    public String registerUser() {
        return "registration-form";
    }

    @PostMapping("/saveRegisteredUser")
    public String saveRegisteredUser(@RequestParam String name,
                                     @RequestParam String email,
                                     @RequestParam String password,
                                     @RequestParam String confirmPassword) {

        if (userService.getUserByEmail(email) != null ||
                !password.equals(confirmPassword) || name==null || name.trim().isEmpty()) {
            return "registration-form";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword("{noop}"+password);
        userService.saveUser(user);

        return "redirect:/showLoginPage";
    }
}
