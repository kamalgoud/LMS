package com.varthana.admin.controller;

import com.varthana.admin.dto.PurchaseBookRequestDto;
import com.varthana.admin.entity.*;
import com.varthana.admin.enums.Transaction;
import com.varthana.admin.exception.CustomException;
import com.varthana.admin.service.BookDetailService;
import com.varthana.admin.service.BookPurchaseTransactionService;
import com.varthana.admin.service.BookRentTransactionService;
import com.varthana.admin.service.BookTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RestController
public class BookPurchaseRestController {

    @Autowired
    private BookDetailService bookDetailService;
    @Autowired
    private BookPurchaseTransactionService bookPurchaseTransactionService;
    @Autowired
    private BookTransactionService bookTransactionService;
    private Logger logger = LogManager.getLogger(BookPurchaseRestController.class);

    @PostMapping("/purchase-book")
    public Boolean purchaseBook(@RequestBody PurchaseBookRequestDto purchaseBookRequestDto) throws CustomException {
        try {
            Integer bookId = purchaseBookRequestDto.getBookId();
            Integer userId = purchaseBookRequestDto.getUserId();
            String userName = purchaseBookRequestDto.getUserName();
            Long requestedQuantity = purchaseBookRequestDto.getQuantity();
            Boolean isEliteUser = purchaseBookRequestDto.getIsEliteUser();

            BookDetail bookDetail = bookDetailService.getBookById(bookId);
            BookQuantity bookQuantity = bookDetail.getBookQuantity();
            if (bookQuantity.getRemainingQuantity() < requestedQuantity) {
                return false;
            }

            BookPurchaseTransaction bookPurchaseTransaction = new BookPurchaseTransaction();
            bookPurchaseTransaction.setBookId(bookId);
            bookPurchaseTransaction.setBookName(bookDetail.getName());
            bookPurchaseTransaction.setPurchasedDate(LocalDate.now());
            bookPurchaseTransaction.setUserId(userId);
            bookPurchaseTransaction.setUserName(userName);
            bookPurchaseTransaction.setQuantity(requestedQuantity);
            bookPurchaseTransaction.setBookPurchaseTime(LocalDateTime.now());

            if (isEliteUser) {
                bookPurchaseTransaction.setAmountPaid(requestedQuantity * bookDetail.getPrice() * 0.8);
            } else {
                bookPurchaseTransaction.setAmountPaid(requestedQuantity * bookDetail.getPrice());
            }
            bookPurchaseTransactionService.savePurchaseTransaction(bookPurchaseTransaction);

            bookQuantity.setPurchasedQuantity(bookQuantity.getPurchasedQuantity() + requestedQuantity);
            bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity() - requestedQuantity);

            BookTransaction bookTransaction = new BookTransaction();
            bookTransaction.setBookId(bookId);
            bookTransaction.setBookName(bookDetail.getName());
            bookTransaction.setType(Transaction.PURCHASE);
            bookTransaction.setUserId(userId);
            bookTransaction.setPrice(bookDetail.getPrice());
            bookTransaction.setTransactionDate(LocalDate.now());
            bookTransaction.setAmountPaid(bookPurchaseTransaction.getAmountPaid());
            bookTransaction.setTransactionTime(bookPurchaseTransaction.getBookPurchaseTime());
            bookTransaction.setTotalQuantity(bookQuantity.getTotalQuantity());
            bookTransaction.setRentedQuantity(bookQuantity.getRentedQuantity());
            bookTransaction.setRemainingQuantity(bookQuantity.getRemainingQuantity());
            bookTransaction.setFine((double) 0);
            bookTransaction.setRentAmount(Double.valueOf(0));
            bookTransaction.setPurchasedQuantity(requestedQuantity);

            bookTransactionService.saveTransaction(bookTransaction);

            List<BookPurchaseTransaction> bookPurchaseTransactionList = bookDetail.getBookPurchaseTransactions();
            if (bookPurchaseTransactionList == null) {
                bookPurchaseTransactionList = new ArrayList<>();
                bookPurchaseTransactionList.add(bookPurchaseTransaction);
                bookDetail.setBookPurchaseTransactions(bookPurchaseTransactionList);
            } else {
                bookPurchaseTransactionList.add(bookPurchaseTransaction);
                bookDetail.setBookPurchaseTransactions(bookPurchaseTransactionList);
            }

            List<BookTransaction> bookTransactionList = bookDetail.getBookTransactions();
            if (bookTransactionList == null) {
                bookTransactionList = new ArrayList<>();
                bookTransactionList.add(bookTransaction);
                bookDetail.setBookTransactions(bookTransactionList);
            } else {
                bookTransactionList.add(bookTransaction);
                bookDetail.setBookTransactions(bookTransactionList);
            }

            bookDetailService.saveBook(bookDetail);

            return true;
        } catch (Exception e) {
            logger.error("Error while puchasing book : {}", e.getMessage());
            throw new CustomException("Error while purchasing Book " + e.getMessage());
        }
    }

    @GetMapping("/get-purchased-books/{id}")
    public List<BookPurchaseTransaction> getPurchasedBooks(@PathVariable("id") Integer userId) throws CustomException {
        try {
            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionService
                    .getPurchaseTransactionsByUserId(userId);
            return bookPurchaseTransactions;
        } catch (Exception e) {
            logger.error("Error while getting purchased books : {}", e.getMessage());
            throw new CustomException("Error while getting purchased books by user id " + e.getMessage());
        }
    }
}
