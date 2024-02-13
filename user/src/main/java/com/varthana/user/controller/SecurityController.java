package com.varthana.user.controller;

import com.varthana.user.entity.User;
import com.varthana.user.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private Logger logger = LogManager.getLogger(SecurityController.class);

    @GetMapping("/showLoginPage")
    public String showLoginPage() {
        try{
            return "login";
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @RequestMapping("/access-denied")
    public String accessDenied() {
        try {
            return "access-denied";
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/register")
    public String registerUser() {
        try {
            return "registration-form";
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @PostMapping("/saveRegisteredUser")
    public String saveRegisteredUser(@RequestParam String name,
                                     @RequestParam String email,
                                     @RequestParam String password,
                                     @RequestParam String confirmPassword) {
        try {
            if (userService.getUserByEmail(email) != null ||
                    !password.equals(confirmPassword) || name == null || name.trim().isEmpty()) {
                return "registration-form";
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword("{noop}" + password);
            userService.saveUser(user);

            return "redirect:/showLoginPage";
        }catch (Exception e){
            logger.error("error while registering user : {}",e.getMessage());
            return "login";
        }
    }
}
