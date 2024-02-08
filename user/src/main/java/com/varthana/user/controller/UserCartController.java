package com.varthana.user.controller;

import com.varthana.user.dto.BookCartQuantityDto;
import com.varthana.user.dto.CartDto;
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

import java.util.List;

@Controller
public class UserCartController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;

    @PostMapping("/cart-quantity")
    public String cartBook(Model model,
                           @RequestParam("id") int id,
                           @RequestParam("name") String name){
        try {
            model.addAttribute("id",id);
            model.addAttribute("name",name);
            return "cart-quantity";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/add-to-cart")
    public String addToCart(Model model,
                            @RequestParam("id") int id,
                            @RequestParam("quantity") long quantity){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            BookCartQuantityDto bookCartQuantityDto = new BookCartQuantityDto(id,user.getId(),quantity);

            String url = "http://localhost:8080/add-to-cart" ;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(bookCartQuantityDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isAddedToCart = response.getBody();
            if(!isAddedToCart){
                return "not-added-to-cart";
            }
            return "redirect:/view-cart";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/view-cart")
    public String viewCart(Model model){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = "http://localhost:8080/get-cart-books/"+user.getId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET,entity,List.class);
            List<CartDto> rentedBooksDtos = response.getBody();
            model.addAttribute("books", rentedBooksDtos);
            return "cart";
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/remove-from-cart")
    public String removeFromCart(Model model,
                                 @RequestParam("id") int bookId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            BookCartQuantityDto bookCartQuantityDto = new BookCartQuantityDto(bookId,user.getId(),0);

            String url = "http://localhost:8080/remove-from-cart" ;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(bookCartQuantityDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isDeleted = response.getBody();
            if(isDeleted){
                return "redirect:/view-cart";
            }
            return null;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/change-quantity")
    public String changeQuantity(Model model,
                                 @RequestParam("id") int bookId,
                                 @RequestParam("name") String name,
                                 @RequestParam("quantity") long quantity){
        try{
            model.addAttribute("bookId",bookId);
            model.addAttribute("name",name);
            model.addAttribute("quantity",quantity);
            System.out.println(bookId+" "+name+" "+quantity);
            return "change-quantity";
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/update-quantity")
    public String updateQuantity(Model model,
                                 @RequestParam("bookId") int bookId,
                                 @RequestParam("quantity") long quantity){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            System.out.println("Updating quantity");

            BookCartQuantityDto bookCartQuantityDto = new BookCartQuantityDto(bookId,user.getId(),quantity);

            String url = "http://localhost:8080/add-to-cart" ;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(bookCartQuantityDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isAddedToCart = response.getBody();
            if(!isAddedToCart){
                return "not-added-to-cart";
            }
            return "redirect:/view-cart";
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
