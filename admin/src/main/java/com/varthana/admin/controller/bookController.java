package com.varthana.admin.controller;

import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.entity.BookQuantity;
import com.varthana.admin.service.BookDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Controller
public class bookController {
    @Autowired
    private BookDetailService bookDetailService;

    @GetMapping("/")
    public String home(Model model){
        List<BookDetail> books = bookDetailService.getAllBooks();
        Iterator<BookDetail> iterator = books.iterator();
        while (iterator.hasNext()) {
            BookDetail book = iterator.next();
            if (book.isDeletedByAdmin()) {
                iterator.remove();  // Safe removal using Iterator
            }
        }
        model.addAttribute("books",books);
        return "home";
    }
    @PostMapping("/create-book")
    public String saveBookDetail(@RequestParam("name") String name,
                                 @RequestParam("author") String author,
                                 @RequestParam("price") int price,
                                 @RequestParam("quantity") int quantity){
        LocalDate localDate = LocalDate.now();
        BookDetail bookDetail = new BookDetail();
        bookDetail.setName(name);
        bookDetail.setAuthor(author);
        bookDetail.setPrice(price);
        bookDetail.setQuantity(quantity);
        bookDetail.setCreatedAt(localDate);
        BookQuantity bookRent = new BookQuantity();
        bookRent.setTotalQuantity(quantity);
        bookRent.setRemainingQuantity(quantity);
        bookDetail.setBookRent(bookRent);
        BookDetail savedBook = bookDetailService.saveBook(bookDetail);
        return "redirect:/";
    }

    @PostMapping("/update-book")
    public String updateBookDetail(@RequestParam("id") int id,Model model){
        BookDetail bookDetail = bookDetailService.getBookById(id);
        model.addAttribute("book",bookDetail);
        return "update-book";
    }

    @PostMapping("/save-updated-book")
    public String saveUpdatedBookDetail(@RequestParam("id") int id,@ModelAttribute BookDetail updatedBookDetail){
        LocalDate localDate = LocalDate.now();
        BookDetail bookDetail = bookDetailService.getBookById(id);
        bookDetail.setName(updatedBookDetail.getName());
        bookDetail.setAuthor(updatedBookDetail.getAuthor());
        bookDetail.setPrice(updatedBookDetail.getPrice());
        bookDetail.setQuantity(updatedBookDetail.getQuantity());
        bookDetail.setUpdatedAt(localDate);
        BookQuantity bookRent = bookDetail.getBookRent();
        bookRent.setTotalQuantity(updatedBookDetail.getQuantity());
        bookDetail.setBookRent(bookRent);
        bookDetailService.updateBook(bookDetail);
        return "redirect:/";
    }

    @PostMapping("/delete-book")
    public String deleteBookDetail(@RequestParam("id") int id){
        if(bookDetailService.isPresentById(id)){
            BookDetail bookDetail = bookDetailService.getBookById(id);
            bookDetail.setDeletedByAdmin(true);
            bookDetailService.saveBook(bookDetail);
        }
        return "redirect:/";
    }

}
