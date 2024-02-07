package com.varthana.admin.controller;

import com.varthana.admin.dto.DateTimeDto;
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

@RestController
public class BookRestController {
    @Autowired
    private BookDetailService bookDetailService;
    @Autowired
    private BookRentTransactionService bookRentTransactionService;
    @GetMapping("/getAllBooks")
    public List<BookDetail> getAllBooks(){
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

    @PostMapping("/rent-book/{id}")
    public BookDetail rentBook(@PathVariable("id")String id, @RequestBody DateTimeDto dateTimeDto){
        int bookId = Integer.valueOf(id);
        BookDetail bookDetail = bookDetailService.getBookById(bookId);
        BookQuantity bookQuantity = bookDetail.getBookQuantity();
        LocalDate startDate = dateTimeDto.getStartDate();
        LocalDate expectedEndDate = dateTimeDto.getEndDate();
        if(bookQuantity.getRemainingQuantity()>0){
            bookQuantity.setRentedQuantity(bookQuantity.getRentedQuantity()+1);
            bookQuantity.setRemainingQuantity(bookQuantity.getRemainingQuantity()-1);
            long daysDifference = ChronoUnit.DAYS.between(startDate, expectedEndDate);
            int amountToBePaid = (int)((Math.ceil(daysDifference/7))*(0.1*bookDetail.getPrice()));
//            bookQuantity.getUserIds().add(userId);
            bookDetail.setBookQuantity(bookQuantity);
            bookDetailService.saveBook(bookDetail);
            BookRentTransaction bookRentTransaction = new BookRentTransaction();
            bookRentTransaction.setBookId(bookId);
            bookRentTransaction.setRentedDate(startDate);
            bookRentTransaction.setExpectedReturnDate(expectedEndDate);
            bookRentTransaction.setRentAmount(amountToBePaid);
            bookRentTransactionService.saveRentTransaction(bookRentTransaction);
            return bookDetail;
        }
        else{
            return null;
        }
    }

    @GetMapping("/get-rented-books")
    public List<BookDetail> getRentedBooks(){
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
}
