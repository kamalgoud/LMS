package com.varthana.admin.controller;

import com.varthana.admin.dto.BookRentRequestDto;
import com.varthana.admin.dto.ReturnBookDto;
import com.varthana.admin.dto.UserBookDto;
import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.entity.BookQuantity;
import com.varthana.admin.entity.BookRentTransaction;
import com.varthana.admin.entity.BookTransaction;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class BookRentalRestController {
    @Autowired
    private BookDetailService bookDetailService;
    @Autowired
    private BookRentTransactionService bookRentTransactionService;
    @Autowired
    private BookTransactionService bookTransactionService;
    private Logger logger = LogManager.getLogger(BookRentalRestController.class);

    @PostMapping("/rent-book")
    public BookDetail rentBook(@RequestBody BookRentRequestDto bookRentRequestDto) throws CustomException {
        try {
            int bookId = bookRentRequestDto.getBookId();
            int userId = bookRentRequestDto.getUserId();
            String userName = bookRentRequestDto.getUserName();
            LocalDate startDate = bookRentRequestDto.getStartDate();
            LocalDate expectedEndDate = bookRentRequestDto.getEndDate();
            boolean isEliteUser = bookRentRequestDto.isEliteUser();

            BookDetail bookDetail = bookDetailService.getBookById(bookId);
            BookQuantity bookQuantity = bookDetail.getBookQuantity();

            if (bookQuantity.getRemainingQuantity() > 0) {
                bookQuantity.setRentedQuantity(bookQuantity.getRentedQuantity() + 1);
                bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity() - 1);

                long daysDifference = ChronoUnit.DAYS.between(startDate, expectedEndDate);
                double amountToBePaid = 0;

                if (daysDifference <= 7) {
                    amountToBePaid = Math.ceil(0.1 * bookDetail.getPrice());
                    if (isEliteUser) {
                        amountToBePaid *= 0.8;
                    }
                } else {
                    amountToBePaid = ((Math.ceil((double) daysDifference / 7)) * ((0.1 * bookDetail.getPrice())));
                    if (isEliteUser) {
                        amountToBePaid *= 0.8;
                    }
                }

                BookRentTransaction bookRentTransaction = new BookRentTransaction();
                bookRentTransaction.setBookId(bookId);
                bookRentTransaction.setUserId(userId);
                bookRentTransaction.setBookName(bookDetail.getName());
                bookRentTransaction.setRentedDate(startDate);
                bookRentTransaction.setUserName(userName);
                bookRentTransaction.setExpectedReturnDate(expectedEndDate);
                bookRentTransaction.setRentAmount(amountToBePaid);
                bookRentTransaction.setPrice(bookDetail.getPrice());
                bookRentTransaction.setTransactionId(UUID.randomUUID());
                bookRentTransaction.setRentTransactionTime(LocalDateTime.now());
                bookRentTransactionService.saveRentTransaction(bookRentTransaction);

                BookTransaction bookTransaction = new BookTransaction();
                bookTransaction.setBookId(bookId);
                bookTransaction.setBookName(bookDetail.getName());
                bookTransaction.setType(Transaction.RENT);
                bookTransaction.setUserId(userId);
                bookTransaction.setTransactionDate(startDate);
                bookTransaction.setPrice(bookDetail.getPrice());
                bookTransaction.setRentAmount(amountToBePaid);
                bookTransaction.setTransactionTime(bookRentTransaction.getRentTransactionTime());
                bookTransaction.setTotalQuantity(bookQuantity.getTotalQuantity());
                bookTransaction.setRentedQuantity(bookQuantity.getRentedQuantity());
                bookTransaction.setRemainingQuantity(bookQuantity.getRemainingQuantity());
                bookTransactionService.saveTransaction(bookTransaction);

                List<BookRentTransaction> bookRentTransactionList = bookDetail.getBookRentTransactions();
                if(bookRentTransactionList==null){
                    bookRentTransactionList = new ArrayList<>();
                    bookRentTransactionList.add(bookRentTransaction);
                    bookDetail.setBookRentTransactions(bookRentTransactionList);
                }
                else{
                    bookRentTransactionList.add(bookRentTransaction);
                    bookDetail.setBookRentTransactions(bookRentTransactionList);
                }

                List<BookTransaction> bookTransactionList = bookDetail.getBookTransactions();
                if(bookTransactionList==null){
                    bookTransactionList = new ArrayList<>();
                    bookTransactionList.add(bookTransaction);
                    bookDetail.setBookTransactions(bookTransactionList);
                }
                else{
                    bookTransactionList.add(bookTransaction);
                    bookDetail.setBookTransactions(bookTransactionList);
                }

                bookDetailService.saveBook(bookDetail);

                return bookDetail;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error while renting a book : {}",e.getMessage());
            throw new CustomException("Error while renting a book "+e.getMessage());
        }
    }

    @PostMapping("/check-user-rented-book")
    public Boolean checkIfUserRentedBook(@RequestBody UserBookDto userBookDto) throws CustomException {
        try {
            int bookId = userBookDto.getBookId();
            int userId = userBookDto.getUserId();
            List<BookRentTransaction> bookRentTransaction = bookRentTransactionService
                    .checkTransactionByBookIdAndUserId(bookId, userId);
            if (bookRentTransaction != null) {
                for (BookRentTransaction bookRentTransaction1 : bookRentTransaction) {
                    if (bookRentTransaction1.getReturnDate() == null) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("Error while checking if user rented a book : {}",e.getMessage());
            throw new CustomException("Error while checking if user rented a book "+e.getMessage());
        }
    }

    @GetMapping("/get-rented-books/{id}")
    public List<BookRentTransaction> getRentedBooks(@PathVariable("id") int id) throws CustomException {
        try {
            List<BookRentTransaction> books = bookRentTransactionService.getBookTransactionsByUserId(id);
            return books;
        } catch (Exception e) {
            logger.error("Error while getting rented books : {}",e.getMessage());
            throw new CustomException("Error while getting rented books");
        }
    }

    @PostMapping("/return-book")
    public Boolean returnBook(@RequestBody ReturnBookDto returnBookDto) throws CustomException {
        try {
            int bookId = returnBookDto.getBookId();
            UUID transactionId = returnBookDto.getTransactionId();
            boolean isEliteUser = returnBookDto.isEliteUser();

            BookRentTransaction bookRentTransaction = bookRentTransactionService
                    .getTransactionByBookIdAndTansactionId(bookId, transactionId);
            if (bookRentTransaction.getReturnDate() != null) {
                return false;
            } else {
                LocalDate returnDate = LocalDate.now();
                bookRentTransaction.setReturnDate(returnDate);
                LocalDate startDate = bookRentTransaction.getRentedDate();
                LocalDate expectedEndDate = bookRentTransaction.getExpectedReturnDate();

                if (returnDate.isAfter(expectedEndDate)) {
                    long daysDifference = ChronoUnit.DAYS.between(expectedEndDate, returnDate);
                    double fine = 0;
                    if (daysDifference <= 7) {
                        fine = Math.ceil(0.2 * bookRentTransaction.getPrice());

                        if (isEliteUser) {
                            fine *= 0.8;
                        }
                    } else {
                        fine = ((Math.ceil((double) daysDifference / 7)) * ((0.2 * bookRentTransaction.getPrice())));

                        if (isEliteUser) {
                            fine *= 0.8;
                        }

                    }
                    bookRentTransaction.setFineAmount(fine);
                }
                bookRentTransaction.setReturnTransactionTime(LocalDateTime.now());
                bookRentTransactionService.saveRentTransaction(bookRentTransaction);

                BookDetail bookDetail = bookDetailService.getBookById(bookId);
                BookQuantity bookQuantity = bookDetail.getBookQuantity();
                bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity() + 1);
                bookQuantity.setRentedQuantity(bookQuantity.getRentedQuantity() - 1);

                BookTransaction bookTransaction = new BookTransaction();
                bookTransaction.setBookId(bookId);
                bookTransaction.setBookName(bookDetail.getName());
                bookTransaction.setType(Transaction.RETURN);
                bookTransaction.setUserId(bookRentTransaction.getUserId());
                bookTransaction.setPrice(bookDetail.getPrice());
                bookTransaction.setRentAmount(bookRentTransaction.getRentAmount());
                bookTransaction.setFine(bookRentTransaction.getFineAmount());
                bookTransaction.setTransactionDate(bookRentTransaction.getReturnDate());
                bookTransaction.setTransactionTime(bookRentTransaction.getReturnTransactionTime());
                bookTransaction.setTotalQuantity(bookQuantity.getTotalQuantity());
                bookTransaction.setRentedQuantity(bookQuantity.getRentedQuantity());
                bookTransaction.setRemainingQuantity(bookQuantity.getRemainingQuantity());
                bookTransactionService.saveTransaction(bookTransaction);

                List<BookTransaction> bookTransactionList = bookDetail.getBookTransactions();
                if(bookTransactionList==null){
                    bookTransactionList = new ArrayList<>();
                    bookTransactionList.add(bookTransaction);
                    bookDetail.setBookTransactions(bookTransactionList);
                }
                else{
                    bookTransactionList.add(bookTransaction);
                    bookDetail.setBookTransactions(bookTransactionList);
                }

                bookDetailService.saveBook(bookDetail);

                return true;
            }
        } catch (Exception e) {
            logger.error("Error while returning a book : {}",e.getMessage());
            throw new CustomException("Error while returning a book "+e.getMessage());
        }
    }
}
