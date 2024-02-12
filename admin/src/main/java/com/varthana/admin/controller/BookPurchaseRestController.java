package com.varthana.admin.controller;

import com.varthana.admin.dto.PurchaseBookRequestDto;
import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.entity.BookPurchaseTransaction;
import com.varthana.admin.entity.BookQuantity;
import com.varthana.admin.entity.BookTransaction;
import com.varthana.admin.enums.Transaction;
import com.varthana.admin.service.BookDetailService;
import com.varthana.admin.service.BookPurchaseTransactionService;
import com.varthana.admin.service.BookRentTransactionService;
import com.varthana.admin.service.BookTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PostMapping("/purchase-book")
    public Boolean purchaseBook(@RequestBody PurchaseBookRequestDto purchaseBookRequestDto) {
        try {
            int bookId = purchaseBookRequestDto.getBookId();
            int userId = purchaseBookRequestDto.getUserId();
            String userName = purchaseBookRequestDto.getUserName();
            long requestedQuantity = purchaseBookRequestDto.getQuantity();
            boolean isEliteUser = purchaseBookRequestDto.isEliteUser();

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
            if (isEliteUser) {
                bookPurchaseTransaction.setAmountPaid(requestedQuantity * bookDetail.getPrice() * 0.8);
            } else {
                bookPurchaseTransaction.setAmountPaid(requestedQuantity * bookDetail.getPrice());
            }
            bookPurchaseTransactionService.savePurchaseTransaction(bookPurchaseTransaction);

            bookQuantity.setPurchasedQuantity(bookQuantity.getPurchasedQuantity() + requestedQuantity);
            bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity() - requestedQuantity);
            bookDetail.setBookQuantity(bookQuantity);

            BookTransaction bookTransaction = new BookTransaction();
            bookTransaction.setBookId(bookId);
            bookTransaction.setBookName(bookDetail.getName());
            bookTransaction.setType(Transaction.PURCHASE);
            bookTransaction.setUserId(userId);
            bookTransaction.setPrice(bookDetail.getPrice());
            bookTransaction.setTransactionDate(LocalDate.now());
            bookTransaction.setAmountPaid(bookPurchaseTransaction.getAmountPaid());
            bookTransactionService.saveTransaction(bookTransaction);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/get-purchased-books/{id}")
    public List<BookPurchaseTransaction> getPurchasedBooks(@PathVariable("id") int userId) {
        try {
            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionService
                    .getPurchaseTransactionsByUserId(userId);
            return bookPurchaseTransactions;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
