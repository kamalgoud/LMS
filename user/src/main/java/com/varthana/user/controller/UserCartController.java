package com.varthana.user.controller;

import com.varthana.user.entity.CartBook;
import com.varthana.user.entity.User;
import com.varthana.user.exception.CustomException;
import com.varthana.user.service.CartBookService;
import com.varthana.user.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Logger logger = LogManager.getLogger(UserCartController.class);

    @PostMapping("/cart-quantity")
    public String cartBook(Model model,
                           @RequestParam("id") Integer id,
                           @RequestParam("price") Double price,
                           @RequestParam("name") String name) throws CustomException {
        try {
            model.addAttribute("id", id);
            model.addAttribute("price", price);
            model.addAttribute("name", name);

            logger.warn("cart-quantity controller : {}", name);

            return "cart-quantity";
        } catch (Exception e) {
            logger.error("error while fetching cart quantity : {}", e.getMessage());
            throw new CustomException("Error while adding cart quantity " + e.getMessage());
        }
    }

    @PostMapping("/add-to-cart")
    public String addToCart(Model model,
                            @RequestParam("id") Integer bookId,
                            @RequestParam("name") String bookName,
                            @RequestParam("price") Double price,
                            @RequestParam("quantity") Long quantity) throws CustomException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            CartBook existedCartBook = cartService.getCartBookByBookIdAndUserId(bookId, user.getId());

            if (existedCartBook != null) {
                existedCartBook.setAmountToBePaid(price * quantity);
                existedCartBook.setQuantityWanted(quantity);
                cartService.saveCart(existedCartBook);

                logger.warn("add-to-cart controller existed book : {}", existedCartBook.toString());
            } else {
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

                logger.warn("add-to-cart controller book : {}", cartBook.toString());
            }

            return "redirect:/view-cart";
        } catch (Exception e) {
            logger.error("error while adding book to cart : {}", e.getMessage());
            throw new CustomException("Error while adding book to cart " + e.getMessage());
        }
    }

    @GetMapping("/view-cart")
    public String viewCart(Model model) throws CustomException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            List<CartBook> cartBookList = user.getCartBooks();
            model.addAttribute("books", cartBookList);

            logger.warn("view-cart controller : {}", cartBookList);

            return "cart";
        } catch (Exception e) {
            logger.error("error while viewing cart : {}", e.getMessage());
            throw new CustomException("Error while accessing cart " + e.getMessage());
        }
    }

    @PostMapping("/remove-from-cart")
    public String removeFromCart(Model model,
                                 @RequestParam("id") Integer bookId) throws CustomException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            CartBook cartBook = cartService.getCartBookByBookIdAndUserId(bookId, user.getId());
            List<CartBook> cartBookList = user.getCartBooks();
            cartBookList.remove(cartBook);
            cartService.deleteCart(cartBook);
            userService.saveUser(user);

            logger.warn("remove-from-cart controller : {}", bookId);

            return "redirect:/view-cart";
        } catch (Exception e) {
            logger.error("error while removing from cart : {}", e.getMessage());
            throw new CustomException("Error while removing book from cart " + e.getMessage());
        }
    }

    @PostMapping("/change-quantity")
    public String changeQuantity(Model model,
                                 @RequestParam("id") Integer bookId,
                                 @RequestParam("name") String name,
                                 @RequestParam("quantity") Long quantity) throws CustomException {
        try {
            model.addAttribute("bookId", bookId);
            model.addAttribute("name", name);
            model.addAttribute("quantity", quantity);

            logger.warn("change-quantity controller : {}", quantity);
            return "change-quantity";
        } catch (Exception e) {
            logger.error("error while changing quantity : {}", e.getMessage());
            throw new CustomException("Error while changing book quantity in cart " + e.getMessage());
        }
    }

    @PostMapping("/update-quantity")
    public String updateQuantity(Model model,
                                 @RequestParam("bookId") Integer bookId,
                                 @RequestParam("quantity") Long quantity) throws CustomException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(authentication.getName());

            CartBook cartBook = cartService.getCartBookByBookIdAndUserId(bookId, user.getId());
            cartBook.setQuantityWanted(quantity);
            cartBook.setAmountToBePaid(quantity * cartBook.getPrice());
            cartService.saveCart(cartBook);

            logger.warn("update-quantity controller : {}", bookId);
            return "redirect:/view-cart";
        } catch (Exception e) {
            logger.error("error while updating the quantity : {}", e.getMessage());
            throw new CustomException("Error while updating bokk quantity in cart " + e.getMessage());
        }
    }
}
