package com.varthana.user.controller;

import com.varthana.user.dto.BookDetailDto;
import com.varthana.user.dto.PurchaseBookRequestDto;
import com.varthana.user.dto.PurchasedBooksDto;
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

import java.util.List;

@Controller
public class UserPurchaseBooksController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private CartBookService cartBookService;

    @PostMapping("/purchase-book")
    public String purchase(Model model,
                           @RequestParam("bookId") int bookId,
                           @RequestParam("quantity") long quantity) {
        try {
            if(quantity==0){
                return "not-able-to-purchase";
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = "http://localhost:8080/purchase-book";

            PurchaseBookRequestDto purchaseBookRequestDto = new PurchaseBookRequestDto(bookId, user.getId(),
                    user.getName(), quantity, user.isEliteUser());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com", "123");

            HttpEntity<Object> entity = new HttpEntity<>(purchaseBookRequestDto, httpHeaders);

            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);
            Boolean isPurchased = response.getBody();

            if (!isPurchased) {
                return "not-able-to-purchase";
            }

            CartBook cartBook = cartBookService.getCartBookByBookIdAndUserId(bookId, user.getId());
            List<CartBook> cartBookList = user.getCartBooks();
            cartBookList.remove(cartBook);
            cartBookService.deleteCart(cartBook);
            userService.saveUser(user);

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/purchased-books")
    public String purchasedBooks(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = "http://localhost:8080/get-purchased-books/" + user.getId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com", "123");

            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            List<PurchasedBooksDto> books = response.getBody();
            if (books == null) {
                return "no-purchased-books";
            }
            model.addAttribute("books", books);
            return "purchased-books";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }
}
