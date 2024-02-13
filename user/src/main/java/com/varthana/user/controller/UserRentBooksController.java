package com.varthana.user.controller;

import com.varthana.user.dto.*;
import com.varthana.user.entity.User;
import com.varthana.user.service.UserService;
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
public class UserRentBooksController {

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

    private Logger logger = LogManager.getLogger(UserRentBooksController.class);

    @PostMapping("/request-rent-book")
    public String rentDetails(Model model,
                              @RequestParam("id") int id,
                              @RequestParam("name") String name) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = adminUrl+"/check-user-rented-book";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(adminUserName, adminPassword);

            UserBookDto userBookDto = new UserBookDto(id, user.getId());
            HttpEntity<Object> entity = new HttpEntity<>(userBookDto, httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);
            Boolean isBookRentedByUser = response.getBody();

            if (isBookRentedByUser) {
                model.addAttribute("warning","Already Rented");
                logger.warn("request-rent-book controller : {}",isBookRentedByUser);
                return "warning";
            } else {
                model.addAttribute("id", id);
                model.addAttribute("name", name);
                logger.warn("request-rent-book controller : {}",isBookRentedByUser);
                return "rent-details";
            }
        } catch (Exception e) {
            logger.error("error while requesting to rent book : {}",e.getMessage());
            return "error";
        }
    }

    @PostMapping("/rent-book")
    public String rentBook(Model model,
                           @RequestParam("id") int id,
                           @RequestParam("startdate") LocalDate startDate,
                           @RequestParam("enddate") LocalDate endDate) {
        try {
            if (startDate == null || endDate == null) {
                model.addAttribute("warning","Invalid start date or end date");
                logger.warn("rent-book controller, Empty fields");
                return "warning";
            }

            long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
            if (daysDifference <= 0) {
                model.addAttribute("warning","Start Date is Greater than End Date");
                logger.warn("rent-book controller, book not available : inconsistent dates");
                return "warning";
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            BookRentRequestDto bookRentRequestDto = new BookRentRequestDto(id, user.getId(), user.getName(),
                    startDate, endDate, user.isEliteUser());

            String url = adminUrl+"/rent-book";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(adminUserName, adminPassword);
            HttpEntity<Object> entity = new HttpEntity<>(bookRentRequestDto, httpHeaders);
            ResponseEntity<BookDetailDto> response
                    = restTemplate.exchange(url, HttpMethod.POST, entity, BookDetailDto.class);
            BookDetailDto bookDetailDto = response.getBody();

            if (bookDetailDto == null) {
                model.addAttribute("warning","Books Not Available");
                logger.warn("rent-book controller, book not available : {}",id);
                return "warning";
            }

            logger.warn("rent-book controller : {}",bookDetailDto.toString());
            return "redirect:/";
        } catch (Exception e) {
            logger.error("error while renting : {}",e.getMessage());
            return "error";
        }
    }

    @GetMapping("/rented-books")
    public String getRentedBooks(Model model) {//id
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = adminUrl+"/get-rented-books/" + user.getId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(adminUserName, adminPassword);

            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

            List<RentedBooksDto> rentedBooksDtos = response.getBody();
            model.addAttribute("books", rentedBooksDtos);

            logger.warn("rented-books controller : {}",rentedBooksDtos);

            return "rented-books";
        } catch (Exception e) {
            logger.error("error while retrieving rented books : {}",e.getMessage());
            return "error";
        }
    }

    @PostMapping("/return-book")
    public String returnBook(Model model,
                             @RequestParam("id") int id,
                             @RequestParam("transactionId") UUID transactionId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = adminUrl+"/return-book";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(adminUserName, adminPassword);

            ReturnBookDto returnBookDto = new ReturnBookDto(id, transactionId, user.isEliteUser());
            HttpEntity<Object> entity = new HttpEntity<>(returnBookDto, httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);
            Boolean isAbleToReturnBook = response.getBody();
            if (!isAbleToReturnBook) {
                model.addAttribute("warning","Already-returned");
                logger.warn("return-book controller already returned : {}",id);
                return "warning";
            }

            logger.warn("return-book controller");
            return "redirect:/";
        } catch (Exception e) {
            logger.error("error while returning book : {}",e.getMessage());
            return "error";
        }
    }
}
