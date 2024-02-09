package com.varthana.admin.controller;

import com.varthana.admin.configuration.SecurityConfiguration;
import com.varthana.admin.entity.*;
import com.varthana.admin.service.AdminService;
import com.varthana.admin.service.BookDetailService;
import com.varthana.admin.service.BookPurchaseTransactionService;
import com.varthana.admin.service.BookRentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class BookController {
    @Autowired
    private BookDetailService bookDetailService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private BookRentTransactionService bookRentTransactionService;
    @Autowired
    private BookPurchaseTransactionService bookPurchaseTransactionService;

    @GetMapping("/")
    public String home(Model model) {
        try {
            List<BookDetail> books = bookDetailService.getAllBooks();
            Iterator<BookDetail> iterator = books.iterator();
            while (iterator.hasNext()) {
                BookDetail book = iterator.next();
                if (book.isDeletedByAdmin()) {
                    iterator.remove();  // Safe removal using Iterator
                }
            }
            model.addAttribute("books", books);
            return "home";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/add-book")
    public String addBook() {
        try {
            return "add-book";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/create-book")
    public String saveBookDetail(Model model,
                                 @RequestParam("name") String name,
                                 @RequestParam("author") String author,
                                 @RequestParam("price") int price,
                                 @RequestParam("quantity") int quantity) {
        try {
            if (name == null || author == null || price == 0 || quantity == 0) {
                model.addAttribute("warning","Fill All Details to Create Book");
                return "warning";
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            Admin admin = adminService.getAdminByEmail(authentication.getName());

            LocalDate localDate = LocalDate.now();
            BookDetail bookDetail = new BookDetail();
            bookDetail.setName(name);
            bookDetail.setAuthor(author);
            bookDetail.setPrice(price);
            bookDetail.setQuantity(quantity);
            bookDetail.setCreatedAt(localDate);

            BookQuantity bookQuantity = new BookQuantity();
            bookQuantity.setTotalQuantity(quantity);
            bookQuantity.setRemainingQuantity(quantity);
            bookDetail.setBookQuantity(bookQuantity);

            BookDetail savedBook = bookDetailService.saveBook(bookDetail);

            if (admin.getBookDetails() == null) {
                List<BookDetail> bookDetailList = new ArrayList<>();
                bookDetailList.add(bookDetail);
                admin.setBookDetails(bookDetailList);
                adminService.update(admin);
            } else {
                List<BookDetail> bookDetailList = admin.getBookDetails();
                bookDetailList.add(bookDetail);
                admin.setBookDetails(bookDetailList);
                adminService.update(admin);
            }

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/view-my-books")
    public String viewMyBooks(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Admin admin = adminService.getAdminByEmail(authentication.getName());

            if (admin.getBookDetails() == null || admin.getBookDetails().isEmpty()) {
                model.addAttribute("warning","Books Not Created");
                return "warning";
            }
            List<BookDetail> books = admin.getBookDetails();
            Iterator<BookDetail> iterator = books.iterator();
            while (iterator.hasNext()) {
                BookDetail book = iterator.next();
                if (book.isDeletedByAdmin()) {
                    iterator.remove();  // Safe removal using Iterator
                }
            }
            model.addAttribute("books", books);
            return "my-books";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/update-book")
    public String updateBookDetail(@RequestParam("id") int id, Model model) {
        try {
            BookDetail bookDetail = bookDetailService.getBookById(id);
            model.addAttribute("book", bookDetail);
            return "update-book";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/save-updated-book")
    public String saveUpdatedBookDetail(@RequestParam("id") int id, @ModelAttribute BookDetail updatedBookDetail) {
        try {
            LocalDate localDate = LocalDate.now();
            BookDetail bookDetail = bookDetailService.getBookById(id);
            bookDetail.setName(updatedBookDetail.getName());
            bookDetail.setAuthor(updatedBookDetail.getAuthor());
            bookDetail.setPrice(updatedBookDetail.getPrice());
            bookDetail.setQuantity(updatedBookDetail.getQuantity());
            bookDetail.setUpdatedAt(localDate);
            BookQuantity bookQuantity = bookDetail.getBookQuantity();
            bookQuantity.setTotalQuantity(updatedBookDetail.getQuantity());
            bookQuantity.setRemainingQuantity(updatedBookDetail.getQuantity() - bookQuantity.getRentedQuantity() -
                    bookQuantity.getPurchasedQuantity());

            bookDetail.setBookQuantity(bookQuantity);
            bookDetailService.updateBook(bookDetail);
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/delete-book")
    public String deleteBookDetail(@RequestParam("id") int id) {
        try {
            if (bookDetailService.isPresentById(id)) {
                BookDetail bookDetail = bookDetailService.getBookById(id);
                bookDetail.setDeletedByAdmin(true);
                bookDetailService.saveBook(bookDetail);
            }
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/my-book-rent-transaction")
    public String myBookRentTransactions(Model model, @RequestParam("bookId") int bookId) {
        try {
            List<BookRentTransaction> bookRentTransactions = bookRentTransactionService
                    .getBookTransactionsByBookId(bookId);
            model.addAttribute("books", bookRentTransactions);
            return "my-book-rent-transactions";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/my-book-purchase-transaction")
    public String myBookPurchaseTransactions(Model model, @RequestParam("bookId") int bookId) {
        try {
            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionService
                    .getPurchaseTransactionsByBookId(bookId);
            model.addAttribute("books", bookPurchaseTransactions);
            return "my-book-purchase-transactions";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/my-book-quantity")
    public String myBookQuantity(Model model, @RequestParam("bookId") int bookId) {
        try {
            BookDetail bookDetail = bookDetailService.getBookById(bookId);
            BookQuantity bookQuantity = bookDetail.getBookQuantity();
            model.addAttribute("bookQuantity", bookQuantity);
            model.addAttribute("name", bookDetail.getName());
            return "my-book-quantity-detail";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}
