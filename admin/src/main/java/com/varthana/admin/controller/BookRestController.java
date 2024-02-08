package com.varthana.admin.controller;

import com.varthana.admin.dto.*;
import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.entity.BookQuantity;
import com.varthana.admin.entity.BookRentTransaction;
import com.varthana.admin.service.BookDetailService;
import com.varthana.admin.service.BookRentTransactionService;
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

//            System.out.println(bookQuantity+" "+"bookQuantity  /rent-book  in  BookRestController");

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
            BookRentTransaction bookTransaction = bookRentTransactionService.getTransactionByBookIdAndTansactionId(bookId,
                    transactionId);
            if(bookTransaction.getReturnDate()!=null){
                return false;
            }
            else{
                LocalDate returnDate = LocalDate.now();
                bookTransaction.setReturnDate(returnDate);
                LocalDate startDate = bookTransaction.getRentedDate();
                LocalDate expectedEndDate = bookTransaction.getExpectedReturnDate();


                if(returnDate.isAfter(expectedEndDate)){
                    long daysDifference = ChronoUnit.DAYS.between(expectedEndDate, returnDate);
                    double fine = 0;
                    if(daysDifference<=7){
                        fine = Math.ceil(0.2 * bookTransaction.getPrice());
                    }
                    else{
                        fine =  ((Math.ceil((double)daysDifference / 7)) * ((0.2 * bookTransaction.getPrice())));
                    }
                    System.out.println(0.1*bookTransaction.getPrice());
                    System.out.println(Math.ceil(daysDifference));
                    bookTransaction.setFineAmount(fine);
                }
                bookRentTransactionService.saveRentTransaction(bookTransaction);

                BookDetail bookDetail = bookDetailService.getBookById(bookId);
                BookQuantity bookQuantity = bookDetail.getBookQuantity();
                bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity()+1);
                bookQuantity.setRentedQuantity(bookQuantity.getRentedQuantity()-1);
                bookDetail.setBookQuantity(bookQuantity);
                bookDetailService.saveBook(bookDetail);

                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
