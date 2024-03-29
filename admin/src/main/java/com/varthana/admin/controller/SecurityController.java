package com.varthana.admin.controller;

import com.varthana.admin.entity.Admin;
import com.varthana.admin.exception.CustomException;
import com.varthana.admin.service.AdminService;
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
    private AdminService adminService;
    private Logger logger = LogManager.getLogger(SecurityController.class);

    @GetMapping("/showLoginPage")
    public String showLoginPage() throws CustomException {
        try {
            return "login";
        } catch (Exception e) {
            logger.error("Error while showing Login Page : {}", e.getMessage());
            throw new CustomException("Error while showing login page " + e.getMessage());
        }
    }

    @RequestMapping("/access-denied")
    public String accessDenied() throws CustomException {
        try {
            return "access-denied";
        } catch (Exception e) {
            logger.error("Error while viewing access-denied page : {}", e.getMessage());
            throw new CustomException("Error while access denying " + e.getMessage());
        }
    }

    @GetMapping("/register")
    public String registerUser() throws CustomException {
        try {
            return "registration-form";
        } catch (Exception e) {
            logger.error("Error while accessing /register : {}", e.getMessage());
            throw new CustomException("Error while trying to register " + e.getMessage());
        }
    }

    @PostMapping("/saveRegisteredAdmin")
    public String saveRegisteredUser(@RequestParam String name,
                                     @RequestParam String email,
                                     @RequestParam String password,
                                     @RequestParam String confirmPassword) {

        try {
            if (adminService.getAdminByEmail(email) != null ||
                    !password.equals(confirmPassword) || name == null || name.trim().isEmpty()) {
                return "registration-form";
            }

            Admin admin = new Admin();
            admin.setName(name);
            admin.setEmail(email);
            admin.setPassword("{noop}" + password);
            adminService.saveAdmin(admin);

            return "redirect:/showLoginPage";
        } catch (Exception e) {
            logger.error("Error while registering user : {}", e.getMessage());
            return "login";
        }
    }
}
