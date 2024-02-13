package com.varthana.admin.controller;

import com.varthana.admin.dto.*;
import com.varthana.admin.entity.*;
import com.varthana.admin.enums.Transaction;
import com.varthana.admin.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private Logger logger = LogManager.getLogger(BookRestController.class);

    @GetMapping("/getAllBooks")
    public List<BookDetail> getAllBooks() {
        try {
            List<BookDetail> books = bookDetailService.getAllBooks();
            Iterator<BookDetail> iterator = books.iterator();
            while (iterator.hasNext()) {
                BookDetail book = iterator.next();
                if (book.isDeletedByAdmin()) {
                    iterator.remove();
                }
            }

            return books;
        } catch (Exception e) {
            logger.error("Error while retrieving all books : {}",e.getMessage());
            return null;
        }
    }

    @GetMapping("/all-transactions/{id}")
    public List<BookTransaction> allTransactions(@PathVariable("id") int userId) {
        try {
            return bookTransactionService.getTransactionsByUserId(userId);
        } catch (Exception e) {
            logger.error("Error while getting all transactions : {}",e.getMessage());
            return null;
        }
    }
}
