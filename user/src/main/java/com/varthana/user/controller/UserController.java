package com.varthana.user.controller;

import com.varthana.user.dto.*;
import com.varthana.user.entity.User;
import com.varthana.user.exception.CustomException;
import com.varthana.user.service.UserService;
import com.varthana.user.service.serviceimpl.CartBookServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/")
    public String home(Model model) throws CustomException {
        try {
            String url = adminUrl + "/getAllBooks";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(adminUserName, adminPassword);
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            List<BookDetailDto> bookDetailsDto = response.getBody();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            model.addAttribute("books", bookDetailsDto);
            model.addAttribute("isEliteUser", user.getIsEliteUser());

            logger.warn("home controller : {}", bookDetailsDto.toString());
            return "home";
        } catch (Exception e) {
            logger.error("error wile getting all books : {}", e.getMessage());
            throw new CustomException("Error while getting all books from admin " + e.getMessage());
        }
    }

    @GetMapping("/all-transactions")
    public String allTransactions(Model model) throws CustomException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            String url = adminUrl + "/all-transactions/" + user.getId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(adminUserName, adminPassword);
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            List<BookTransactionsDto> bookTransactionsDto = response.getBody();

            if (bookTransactionsDto == null) {
                model.addAttribute("warning", "No Transactions");
                logger.warn("all-transactions controller : No Transactions");
                return "warning";
            }
            model.addAttribute("books", bookTransactionsDto);

            logger.warn("all-transactions controller : {}", bookTransactionsDto.toString());
            return "all-transactions";
        } catch (Exception e) {
            logger.error("error while getting all transactions : {}", e.getMessage());
            throw new CustomException("Error while retrieving all transactions " + e.getMessage());
        }
    }

    @PostMapping("/become-elite-user")
    public String becomeEliteUser(Model model) throws CustomException {
        try {
            return "become-elite-user";
        } catch (Exception e) {
            throw new CustomException("Error while becoming elite user " + e.getMessage());
        }
    }

    @PostMapping("/elite-user")
    public String eliteUser(Model model) throws CustomException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            user.setIsEliteUser(true);
            userService.saveUser(user);

            logger.warn("become-elite-user controller");
            return "redirect:/";
        } catch (Exception e) {
            throw new CustomException("Error while becoming elite user " + e.getMessage());
        }
    }

    @GetMapping("/popular-books")
    public String popularBooks(Model model) throws CustomException {
        try {
            String url = adminUrl + "/popular-books";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(adminUserName, adminPassword);
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            List<String> popularBooks = response.getBody();

            model.addAttribute("highestRentedBook",popularBooks.get(0));
            model.addAttribute("highestPurchasedBook",popularBooks.get(1));

            return "popular-books";
        } catch (Exception e) {
            throw new CustomException("Error while becoming elite user " + e.getMessage());
        }
    }

}

