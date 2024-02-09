package com.varthana.admin.controller;

import com.varthana.admin.dto.BookRentRequestDto;
import com.varthana.admin.dto.ReturnBookDto;
import com.varthana.admin.dto.UserBookDto;
import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.entity.BookQuantity;
import com.varthana.admin.entity.BookRentTransaction;
import com.varthana.admin.entity.BookTransaction;
import com.varthana.admin.enums.Transaction;
import com.varthana.admin.service.BookDetailService;
import com.varthana.admin.service.BookPurchaseTransactionService;
import com.varthana.admin.service.BookRentTransactionService;
import com.varthana.admin.service.BookTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @PostMapping("/rent-book")
    public BookDetail rentBook(@RequestBody BookRentRequestDto bookRentRequestDto) {
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

                System.out.println((Math.ceil(daysDifference / 7)));

                bookDetail.setBookQuantity(bookQuantity);
                bookDetailService.saveBook(bookDetail);

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
                bookRentTransactionService.saveRentTransaction(bookRentTransaction);

                BookTransaction bookTransaction = new BookTransaction();
                bookTransaction.setBookId(bookId);
                bookTransaction.setBookName(bookDetail.getName());
                bookTransaction.setType(Transaction.RENT);
                bookTransaction.setUserId(userId);
                bookTransaction.setTransactionDate(startDate);
                bookTransaction.setPrice(bookDetail.getPrice());
                bookTransaction.setRentAmount(amountToBePaid);
                bookTransactionService.saveTransaction(bookTransaction);

                return bookDetail;
            } else {
                System.out.println(null + " " + "/rent-book  in  BookRestController");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/check-user-rented-book")
    public Boolean checkIfUserRentedBook(@RequestBody UserBookDto userBookDto) {
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
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/get-rented-books/{id}")
    public List<BookRentTransaction> getRentedBooks(@PathVariable("id") int id) {
        try {
            List<BookRentTransaction> books = bookRentTransactionService.getBookTransactionsByUserId(id);
            System.out.println(books);
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/return-book")
    public Boolean returnBook(@RequestBody ReturnBookDto returnBookDto) {
        try {
            int bookId = returnBookDto.getBookId();
            UUID transactionId = returnBookDto.getTransactionId();
            boolean isEliteUser = returnBookDto.isEliteUser();

            System.out.println(bookId + " " + transactionId);
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
                    System.out.println(daysDifference);
                    double fine = 0;
                    if (daysDifference <= 7) {
                        fine = Math.ceil(0.2 * bookRentTransaction.getPrice());

                        if (isEliteUser) {
                            fine *= 0.8;
                        }
                    } else {
                        System.out.println((double) daysDifference / 7);
                        fine = ((Math.ceil((double) daysDifference / 7)) * ((0.2 * bookRentTransaction.getPrice())));

                        if (isEliteUser) {
                            fine *= 0.8;
                        }

                    }
                    System.out.println(0.1 * bookRentTransaction.getPrice());
                    System.out.println(Math.ceil(daysDifference));
                    bookRentTransaction.setFineAmount(fine);
                }
                bookRentTransactionService.saveRentTransaction(bookRentTransaction);

                BookDetail bookDetail = bookDetailService.getBookById(bookId);
                BookQuantity bookQuantity = bookDetail.getBookQuantity();
                bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity() + 1);
                bookQuantity.setRentedQuantity(bookQuantity.getRentedQuantity() - 1);
                bookDetail.setBookQuantity(bookQuantity);
                bookDetailService.saveBook(bookDetail);

                BookTransaction bookTransaction = new BookTransaction();
                bookTransaction.setBookId(bookId);
                bookTransaction.setBookName(bookDetail.getName());
                bookTransaction.setType(Transaction.RETURN);
                bookTransaction.setUserId(bookRentTransaction.getUserId());
                bookTransaction.setPrice(bookDetail.getPrice());
                bookTransaction.setRentAmount(bookRentTransaction.getRentAmount());
                bookTransaction.setFine(bookRentTransaction.getFineAmount());
                bookTransaction.setTransactionDate(bookRentTransaction.getReturnDate());
                bookTransactionService.saveTransaction(bookTransaction);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
