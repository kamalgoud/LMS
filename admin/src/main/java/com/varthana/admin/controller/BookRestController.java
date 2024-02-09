package com.varthana.admin.controller;

import com.varthana.admin.dto.*;
import com.varthana.admin.entity.*;
import com.varthana.admin.enums.Transaction;
import com.varthana.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
public class BookRestController {
    @Autowired
    private BookDetailService bookDetailService;
    @Autowired
    private BookTransactionService bookTransactionService;

    @GetMapping("/getAllBooks")
    public List<BookDetail> getAllBooks() {
        try {
            List<BookDetail> books = bookDetailService.getAllBooks();
            Iterator<BookDetail> iterator = books.iterator();
            while (iterator.hasNext()) {
                BookDetail book = iterator.next();
                if (book.isDeletedByAdmin()) {
                    iterator.remove();  // Safe removal using Iterator
                }
            }
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/all-transactions/{id}")
    public List<BookTransaction> allTransactions(@PathVariable("id") int userId) {
        try {
            return bookTransactionService.getTransactionsByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
