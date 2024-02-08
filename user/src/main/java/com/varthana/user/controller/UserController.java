package com.varthana.user.controller;

import com.varthana.user.dto.*;
import com.varthana.user.entity.User;
import com.varthana.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public String home(Model model){
        try {
            String url = "http://localhost:8080/getAllBooks";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET,entity,List.class);
            List<BookDetailDto> bookDetailsDto = response.getBody();
            model.addAttribute("books", bookDetailsDto);
            return "home";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}

