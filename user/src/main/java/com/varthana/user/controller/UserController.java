package com.varthana.user.controller;

import com.varthana.user.dto.*;
import com.varthana.user.entity.User;
import com.varthana.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Value("${admin-url}")
    private String adminUrl;
    @Value("${admin-username}")
    private String adminUserName;
    @Value("${admin-password}")
    private String adminPassword;

    @GetMapping("/")
    public String home(Model model) {
        try {
            String url = adminUrl+"/getAllBooks";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(adminUserName, adminPassword);
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            List<BookDetailDto> bookDetailsDto = response.getBody();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            model.addAttribute("books", bookDetailsDto);
            model.addAttribute("isEliteUser", user.isEliteUser());

            return "home";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/all-transactions")
    public String allTransactions(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            String url = adminUrl+"/all-transactions/" + user.getId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(adminUserName, adminPassword);
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            List<BookTransactionsDto> bookTransactionsDto = response.getBody();

            if (bookTransactionsDto == null) {
                model.addAttribute("warning","No Transactions");
                return "warning";
            }
            model.addAttribute("books", bookTransactionsDto);

            return "all-transactions";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/become-elite-user")
    public String becomeEliteUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        user.setEliteUser(true);
        userService.saveUser(user);

        return "redirect:/";
    }
}

