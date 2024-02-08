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
public class UserRentBooksController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;

    @PostMapping("/request-rent-book")
    public String rentDetails(Model model,
                              @RequestParam("id") int id,
                              @RequestParam("name") String name){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = "http://localhost:8080/check-user-rented-book";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            UserBookDto userBookDto = new UserBookDto(id,user.getId());
            HttpEntity<Object> entity = new HttpEntity<>(userBookDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isBookRentedByUser = response.getBody();

            if(isBookRentedByUser){
                return "already-rented";
            }
            else{
                model.addAttribute("id", id);
                model.addAttribute("name", name);
                return "rent-details";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/rent-book")
    public String rentBook(Model model,
                           @RequestParam("id") int id,
                           @RequestParam("startdate") LocalDate startDate,
                           @RequestParam("enddate") LocalDate endDate){
        try {
            long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
            System.out.println(daysDifference+" "+"/rent-book  in  UserController");
            if (daysDifference <= 0) {
                return "error";
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            BookRentRequestDto bookRentRequestDto = new BookRentRequestDto(id,user.getId(),startDate,endDate);

            String url = "http://localhost:8080/rent-book" ;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(bookRentRequestDto,httpHeaders);
            ResponseEntity<BookDetailDto> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,BookDetailDto.class);
            BookDetailDto bookDetailDto = response.getBody();

            System.out.println(bookDetailDto+" "+"/rent-book  in  UserController");

            if(bookDetailDto==null){
                return "books-not-available";
            }

            return "redirect:/";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/rented-books")
    public String getRentedBooks(Model model){//id
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = "http://localhost:8080/get-rented-books/"+user.getId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

//            UserIdDto userIdDto = new UserIdDto(user.getId());
//            System.out.println(userIdDto.getUserId());
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
//            System.out.println("Before");
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET,entity,List.class);
//            System.out.println("After");
            List<RentedBooksDto> rentedBooksDtos = response.getBody();
            model.addAttribute("books", rentedBooksDtos);
            return "rented-books";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/return-book")
    public String returnBook(Model model,
                             @RequestParam("id") int id,
                             @RequestParam("transactionId") UUID transactionId){
        try {
            String url = "http://localhost:8080/return-book";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            ReturnBookDto returnBookDto = new ReturnBookDto(id,transactionId);
            HttpEntity<Object> entity = new HttpEntity<>(returnBookDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isAbleToReturnBook = response.getBody();
            if(!isAbleToReturnBook){
                return "already-returned";
            }
            return "redirect:/";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
