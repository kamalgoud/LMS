package com.varthana.admin.controller;

import com.varthana.admin.dto.*;
import com.varthana.admin.entity.*;
import com.varthana.admin.enums.Transaction;
import com.varthana.admin.exception.CustomException;
import com.varthana.admin.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
public class BookRestController {
    @Autowired
    private BookDetailService bookDetailService;
    @Autowired
    private BookTransactionService bookTransactionService;
    @Autowired
    private BookRentTransactionService bookRentTransactionService;
    @Autowired
    private BookPurchaseTransactionService bookPurchaseTransactionService;
    private Logger logger = LogManager.getLogger(BookRestController.class);

    @GetMapping("/getAllBooks")
    public List<BookDetail> getAllBooks() throws CustomException {
        try {
            List<BookDetail> books = bookDetailService.getAllBooks();
            Iterator<BookDetail> iterator = books.iterator();
            while (iterator.hasNext()) {
                BookDetail book = iterator.next();
                if (book.getIsDeletedByAdmin()) {
                    iterator.remove();
                }
            }

            return books;
        } catch (Exception e) {
            logger.error("Error while retrieving all books : {}", e.getMessage());
            throw new CustomException("Error while getting all books " + e.getMessage());
        }
    }

    @GetMapping("/all-transactions/{id}")
    public List<BookTransaction> allTransactions(@PathVariable("id") Integer userId) throws CustomException {
        try {
            return bookTransactionService.getTransactionsByUserId(userId);
        } catch (Exception e) {
            logger.error("Error while getting all transactions : {}", e.getMessage());
            throw new CustomException("Error while retrieving all transactions " + e.getMessage());
        }
    }

    @GetMapping("/popular-books")
    public List<String> popularBooks() throws CustomException {
        try {
            List<BookRentTransaction> bookRentTransactions = bookRentTransactionService.getAllRentalTransactions();
            HashMap<String, Long> rentsFrequency = new HashMap<>();
            String highestRentedBook = "";

            if (bookRentTransactions != null) {
                long count = 0;
                for (int i = 0; i < bookRentTransactions.size(); i++) {
                    rentsFrequency.put(bookRentTransactions.get(i).getBookName(),
                            rentsFrequency.getOrDefault(bookRentTransactions.get(i).getBookName(), (long)0) + 1);
                    if (rentsFrequency.get(bookRentTransactions.get(i).getBookName()) >= count) {
                        highestRentedBook = bookRentTransactions.get(i).getBookName();
                        count = rentsFrequency.get(bookRentTransactions.get(i).getBookName());
                    }
                }
            }

            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionService.
                    getAllPurchasedBooks();
            HashMap<String, Long> purchaseFrequency = new HashMap<>();
            String highestPurchasedBook = "";

            if (bookPurchaseTransactions != null) {
                long count = 0;
                for (int i = 0; i < bookPurchaseTransactions.size(); i++) {
                    purchaseFrequency.put(bookPurchaseTransactions.get(i).getBookName(),
                            (purchaseFrequency.getOrDefault(bookPurchaseTransactions.get(i).getBookName(), (long)0)
                                    + bookPurchaseTransactions.get(i).getQuantity()));
                    if (purchaseFrequency.get(bookPurchaseTransactions.get(i).getBookName()) >= count) {
                        highestPurchasedBook = bookPurchaseTransactions.get(i).getBookName();
                        count = purchaseFrequency.get(bookPurchaseTransactions.get(i).getBookName());
                    }
                }
            }

            List<String> popularBooks = new ArrayList<>();
            popularBooks.add(highestRentedBook);
            popularBooks.add(highestPurchasedBook);
            return popularBooks;
        }catch (Exception e){
            throw new CustomException("Error while retrieving popular books " + e.getMessage());
        }
    }
}
