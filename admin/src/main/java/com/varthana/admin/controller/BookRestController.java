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
    private BookRentTransactionService bookRentTransactionService;
    @Autowired
    private BookPurchaseTransactionService bookPurchaseTransactionService;
    @Autowired
    private BookTransactionService bookTransactionService;
    @GetMapping("/getAllBooks")
    public List<BookDetail> getAllBooks(){
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
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/rent-book")
    public BookDetail rentBook( @RequestBody BookRentRequestDto bookRentRequestDto){
        try {
            int bookId = bookRentRequestDto.getBookId();
            int userId = bookRentRequestDto.getUserId();
            LocalDate startDate = bookRentRequestDto.getStartDate();
            LocalDate expectedEndDate = bookRentRequestDto.getEndDate();

            BookDetail bookDetail = bookDetailService.getBookById(bookId);
            BookQuantity bookQuantity = bookDetail.getBookQuantity();

            if (bookQuantity.getRemainingQuantity() > 0) {
                bookQuantity.setRentedQuantity(bookQuantity.getRentedQuantity() + 1);
                bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity() - 1);

                long daysDifference = ChronoUnit.DAYS.between(startDate, expectedEndDate);
                double amountToBePaid =  0;

                if(daysDifference<=7){
                    amountToBePaid = Math.ceil(0.1 * bookDetail.getPrice());
                }
                else{
                    amountToBePaid =  ((Math.ceil((double)daysDifference / 7)) * ((0.1 * bookDetail.getPrice())));
                }

                System.out.println((Math.ceil(daysDifference / 7)));

                bookDetail.setBookQuantity(bookQuantity);
                bookDetailService.saveBook(bookDetail);

                BookRentTransaction bookRentTransaction = new BookRentTransaction();
                bookRentTransaction.setBookId(bookId);
                bookRentTransaction.setUserId(userId);
                bookRentTransaction.setBookName(bookDetail.getName());
                bookRentTransaction.setRentedDate(startDate);
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
                System.out.println(null+" "+"/rent-book  in  BookRestController");
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/check-user-rented-book")
    public Boolean checkIfUserRentedBook(@RequestBody UserBookDto userBookDto){
        try {
            int bookId = userBookDto.getBookId();
            int userId = userBookDto.getUserId();
            List<BookRentTransaction> bookRentTransaction = bookRentTransactionService
                    .checkTransactionByBookIdAndUserId(bookId,userId);
            if(bookRentTransaction!=null){
                for(BookRentTransaction bookRentTransaction1:bookRentTransaction){
                    if(bookRentTransaction1.getReturnDate()==null){
                        return true;
                    }
                }
            }
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/get-rented-books/{id}")
    public List<BookRentTransaction> getRentedBooks(@PathVariable("id")int id){
        try {
            List<BookRentTransaction> books = bookRentTransactionService.getBookTransactionsByUserId(id);
            System.out.println(books);
            return books;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/return-book")
    public Boolean returnBook(@RequestBody ReturnBookDto returnBookDto){
        try {
            int bookId = returnBookDto.getBookId();
            UUID transactionId = returnBookDto.getTransactionId();
            System.out.println(bookId+" "+transactionId);
            BookRentTransaction bookRentTransaction = bookRentTransactionService.getTransactionByBookIdAndTansactionId(bookId,
                    transactionId);
            if(bookRentTransaction.getReturnDate()!=null){
                return false;
            }
            else{
                LocalDate returnDate = LocalDate.now();
                bookRentTransaction.setReturnDate(returnDate);
                LocalDate startDate = bookRentTransaction.getRentedDate();
                LocalDate expectedEndDate = bookRentTransaction.getExpectedReturnDate();


                if(returnDate.isAfter(expectedEndDate)){
                    long daysDifference = ChronoUnit.DAYS.between(expectedEndDate, returnDate);
                    double fine = 0;
                    if(daysDifference<=7){
                        fine = Math.ceil(0.2 * bookRentTransaction.getPrice());
                    }
                    else{
                        fine =  ((Math.ceil((double)daysDifference / 7)) * ((0.2 * bookRentTransaction.getPrice())));
                    }
                    System.out.println(0.1*bookRentTransaction.getPrice());
                    System.out.println(Math.ceil(daysDifference));
                    bookRentTransaction.setFineAmount(fine);
                }
                bookRentTransactionService.saveRentTransaction(bookRentTransaction);

                BookDetail bookDetail = bookDetailService.getBookById(bookId);
                BookQuantity bookQuantity = bookDetail.getBookQuantity();
                bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity()+1);
                bookQuantity.setRentedQuantity(bookQuantity.getRentedQuantity()-1);
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
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @PostMapping("/purchase-book")
    public Boolean purchaseBook(@RequestBody PurchaseBookRequestDto purchaseBookRequestDto){
        try{
            int bookId = purchaseBookRequestDto.getBookId();
            int userId = purchaseBookRequestDto.getUserId();
            long requestedQuantity = purchaseBookRequestDto.getQuantity();

            BookDetail bookDetail = bookDetailService.getBookById(bookId);
            BookQuantity bookQuantity = bookDetail.getBookQuantity();
            if(bookQuantity.getRemainingQuantity()<requestedQuantity){
                return false;
            }

            BookPurchaseTransaction bookPurchaseTransaction = new BookPurchaseTransaction();
            bookPurchaseTransaction.setBookId(bookId);
            bookPurchaseTransaction.setBookName(bookDetail.getName());
            bookPurchaseTransaction.setPurchasedDate(LocalDate.now());
            bookPurchaseTransaction.setUserId(userId);
            bookPurchaseTransaction.setQuantity(requestedQuantity);
            bookPurchaseTransaction.setAmountPaid(requestedQuantity*bookDetail.getPrice());
            bookPurchaseTransactionService.savePurchaseTransaction(bookPurchaseTransaction);

            bookQuantity.setPurchasedQuantity(bookQuantity.getPurchasedQuantity()+requestedQuantity);
            bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity()-requestedQuantity);
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
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/get-purchased-books/{id}")
    public List<BookPurchaseTransaction> getPurchasedBooks(@PathVariable("id") int userId){
        try{
            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionService
                    .getPurchaseTransactionsByUserId(userId);
            return bookPurchaseTransactions;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/all-transactions/{id}")
    public List<BookTransaction> allTransactions(@PathVariable("id") int userId){
        try {
            return bookTransactionService.getTransactionsByUserId(userId);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
