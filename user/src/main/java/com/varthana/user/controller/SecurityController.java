package com.varthana.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {
    @GetMapping("/showLoginPage")
    public String showLoginPage() {
        return "login";
    }

    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
