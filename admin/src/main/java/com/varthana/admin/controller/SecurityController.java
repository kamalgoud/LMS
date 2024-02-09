package com.varthana.admin.controller;

import com.varthana.admin.entity.Admin;
import com.varthana.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {
    @Autowired
    private AdminService adminService;
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

    @PostMapping("/saveRegisteredAdmin")
    public String saveRegisteredUser(@RequestParam String name,
                                     @RequestParam String email,
                                     @RequestParam String password,
                                     @RequestParam String confirmPassword) {

        if (adminService.getAdminByEmail(email) != null ||
                !password.equals(confirmPassword) || name==null || name.trim().isEmpty()) {
            return "registration-form";
        }

        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword("{noop}"+password);
        adminService.saveAdmin(admin);

        return "redirect:/showLoginPage";
    }

}
