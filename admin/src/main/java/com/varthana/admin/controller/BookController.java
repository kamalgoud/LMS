package com.varthana.admin.controller;

import com.varthana.admin.configuration.SecurityConfiguration;
import com.varthana.admin.entity.*;
import com.varthana.admin.exception.CustomException;
import com.varthana.admin.service.AdminService;
import com.varthana.admin.service.BookDetailService;
import com.varthana.admin.service.BookPurchaseTransactionService;
import com.varthana.admin.service.BookRentTransactionService;
import com.varthana.admin.service.serviceimpl.AdminServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private Logger logger = LogManager.getLogger(BookController.class);

    @GetMapping("/")
    public String home(Model model) throws CustomException {
        try {
            List<BookDetail> books = bookDetailService.getAllBooks();
            Iterator<BookDetail> iterator = books.iterator();
            while (iterator.hasNext()) {
                BookDetail book = iterator.next();
                if (book.getIsDeletedByAdmin()) {
                    iterator.remove();
                }
            }
            logger.warn("home {}",books);
            System.out.println("Admin Home");
            model.addAttribute("books", books);
            return "home";
        } catch (Exception e) {
            logger.error("Error while loading home page : {}",e.getMessage());
            throw new CustomException("Error while getting books "+e.getMessage());
        }
    }

    @PostMapping("/add-book")
    public String addBook() throws CustomException {
        try {
            return "add-book";
        } catch (Exception e) {
            logger.error("Error while adding book : {}",e.getMessage());
            throw new CustomException("Error while adding books");
        }
    }

    @PostMapping("/create-book")
    public String saveBookDetail(Model model,
                                 @RequestParam("name") String name,
                                 @RequestParam("author") String author,
                                 @RequestParam("price") Double price,
                                 @RequestParam("quantity") Long quantity) throws CustomException {
        try {
            if (name == null || author == null || price == 0 || quantity == 0) {
                model.addAttribute("warning", "Fill All Details to Create Book");
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
            bookQuantity.setRentedQuantity(Long.valueOf(0));
            bookQuantity.setPurchasedQuantity(Long.valueOf(0));
            bookDetail.setBookQuantity(bookQuantity);
            bookDetail.setIsDeletedByAdmin(false);
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
            logger.error("Error while creating book : {}",e.getMessage());
            throw new CustomException("Error while creating admin "+e.getMessage());
        }
    }

    @GetMapping("/view-my-books")
    public String viewMyBooks(Model model) throws CustomException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Admin admin = adminService.getAdminByEmail(authentication.getName());

            if (admin.getBookDetails() == null || admin.getBookDetails().isEmpty()) {
                model.addAttribute("warning", "Books Not Created");
                return "warning";
            }
            List<BookDetail> books = admin.getBookDetails();
            model.addAttribute("books", books);

            return "my-books";
        } catch (Exception e) {
            logger.error("Error while viewing my books : {}",e.getMessage());
            throw new CustomException("Error while viewing my books "+e.getMessage());
        }
    }

    @PostMapping("/update-book")
    public String updateBookDetail(@RequestParam("id") int id, Model model) throws CustomException {
        try {
            BookDetail bookDetail = bookDetailService.getBookById(id);
            if (bookDetail.getIsDeletedByAdmin()) {
                model.addAttribute("warning", "Deleted Book can't be Updated");
                return "warning";
            }
            model.addAttribute("book", bookDetail);

            return "update-book";
        } catch (Exception e) {
            logger.error("Error while updating book : {}",e.getMessage());
            throw new CustomException("Error while updating books "+e.getMessage());
        }
    }

    @PostMapping("/save-updated-book")
    public String saveUpdatedBookDetail(@RequestParam("id") Integer id,
                                        @ModelAttribute BookDetail updatedBookDetail) throws CustomException {
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
            logger.error("Error while saving updated book : {}",e.getMessage());
            throw new CustomException("Error while saving updated book details "+e.getMessage());
        }
    }

    @PostMapping("/delete-book")
    public String deleteBookDetail(Model model, @RequestParam("id") Integer id) throws CustomException {
        try {
            if (bookDetailService.isPresentById(id)) {
                BookDetail bookDetail = bookDetailService.getBookById(id);
                if (bookDetail.getIsDeletedByAdmin()) {
                    model.addAttribute("warning", "Already Deleted ");
                    return "warning";
                }
                bookDetail.setIsDeletedByAdmin(true);
                bookDetailService.saveBook(bookDetail);
            }
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Error while deleting book : {}",e.getMessage());
            throw new CustomException("Error while deleting book "+e.getMessage());
        }
    }

    @GetMapping("/my-book-rent-transaction")
    public String myBookRentTransactions(Model model, @RequestParam("bookId") Integer bookId) throws CustomException {
        try {
            List<BookRentTransaction> bookRentTransactions = bookRentTransactionService
                    .getBookTransactionsByBookId(bookId);
            double totalRentalsIncome = 0;
            for (BookRentTransaction b : bookRentTransactions) {
                totalRentalsIncome += b.getRentAmount() + b.getFineAmount();
            }
            BookDetail bookDetail = bookDetailService.getBookById(bookId);

            model.addAttribute("name",bookDetail.getName());
            model.addAttribute("books", bookRentTransactions);
            model.addAttribute("income", totalRentalsIncome);

            return "my-book-rent-transactions";
        } catch (Exception e) {
            logger.error("Error while viewing my book rent transaction : {}",e.getMessage());
            throw new CustomException("Error while retrieving my book transactions "+e.getMessage());
        }
    }

    @GetMapping("/my-book-purchase-transaction")
    public String myBookPurchaseTransactions(Model model, @RequestParam("bookId") Integer bookId) throws CustomException {
        try {
            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionService
                    .getPurchaseTransactionsByBookId(bookId);
            double totalSalesIncome = 0;
            for (BookPurchaseTransaction b : bookPurchaseTransactions) {
                totalSalesIncome += b.getAmountPaid();
            }
            BookDetail bookDetail = bookDetailService.getBookById(bookId);

            model.addAttribute("name",bookDetail.getName());
            model.addAttribute("books", bookPurchaseTransactions);
            model.addAttribute("income", totalSalesIncome);

            return "my-book-purchase-transactions";
        } catch (Exception e) {
            logger.error("Error while viewing my book purchase transaction : {}",e.getMessage());
            throw new CustomException("Error while retrieving purchase transactions "+e.getMessage());
        }
    }

    @GetMapping("/my-book-quantity")
    public String myBookQuantity(Model model, @RequestParam("bookId") int bookId) throws CustomException {
        try {
            BookDetail bookDetail = bookDetailService.getBookById(bookId);
            if (bookDetail.getIsDeletedByAdmin()) {
                model.addAttribute("warning", "Book Deleted");
                return "warning";
            }
            BookQuantity bookQuantity = bookDetail.getBookQuantity();
            model.addAttribute("bookQuantity", bookQuantity);
            model.addAttribute("name", bookDetail.getName());

            return "my-book-quantity-detail";
        } catch (Exception e) {
            logger.error("Error while viewing my book quantity : {}",e.getMessage());
            throw new CustomException("Error while viewing my book quantity "+e.getMessage());
        }
    }
}
