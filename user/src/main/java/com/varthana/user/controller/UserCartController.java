package com.varthana.user.controller;

import com.varthana.user.dto.BookCartQuantityDto;
import com.varthana.user.dto.CartDto;
import com.varthana.user.entity.CartBook;
import com.varthana.user.entity.User;
import com.varthana.user.service.CartBookService;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserCartController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private CartBookService cartService;

    @PostMapping("/cart-quantity")
    public String cartBook(Model model,
                           @RequestParam("id") int id,
                           @RequestParam("price") double price,
                           @RequestParam("name") String name){
        try {
            model.addAttribute("id",id);
            model.addAttribute("price",price);
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
                            @RequestParam("id") int bookId,
                            @RequestParam("name") String bookName,
                            @RequestParam("price") double price,
                            @RequestParam("quantity") long quantity){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            CartBook existedCartBook = cartService.getCartBookByBookIdAndUserId(bookId, user.getId());
            if(existedCartBook!=null){
                existedCartBook.setAmountToBePaid(price*quantity);
                existedCartBook.setQuantityWanted(quantity);
                cartService.saveCart(existedCartBook);
            }
            else {
                List<CartBook> cartBooks = user.getCartBooks();
                CartBook cartBook = new CartBook();
                cartBook.setName(bookName);
                cartBook.setBookId(bookId);
                cartBook.setPrice(price);
                cartBook.setQuantityWanted(quantity);
                cartBook.setAmountToBePaid(price * quantity);
                cartBook.setUserId(user.getId());
                if (cartBooks != null) {
                    cartBooks.add(cartBook);
                    user.setCartBooks(cartBooks);
                    userService.saveUser(user);
                } else {
                    List<CartBook> cartBookList = new ArrayList<>();
                    cartBookList.add(cartBook);
                    user.setCartBooks(cartBooks);
                    userService.saveUser(user);
                }
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

            List<CartBook> cartBookList = user.getCartBooks();
            model.addAttribute("books", cartBookList);
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
            CartBook cartBook = cartService.getCartBookByBookIdAndUserId(bookId, user.getId());
            List<CartBook> cartBookList = user.getCartBooks();
            cartBookList.remove(cartBook);
            cartService.deleteCart(cartBook);
            userService.saveUser(user);
            return "redirect:/view-cart";
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

            CartBook cartBook = cartService.getCartBookByBookIdAndUserId(bookId, user.getId());
            cartBook.setQuantityWanted(quantity);
            cartBook.setAmountToBePaid(quantity*cartBook.getPrice());
            cartService.saveCart(cartBook);


            return "redirect:/view-cart";
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
