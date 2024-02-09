package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookRentTransaction;
import com.varthana.admin.repository.BookRentTransactionRepository;
import com.varthana.admin.service.BookRentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookRentTransactionServiceImpl implements BookRentTransactionService {
    @Autowired
    private BookRentTransactionRepository bookRentTransactionRepository;
    @Override
    public BookRentTransaction saveRentTransaction(BookRentTransaction bookRentTransaction) {
        return bookRentTransactionRepository.save(bookRentTransaction);
    }

    @Override
    public BookRentTransaction updateRentTransaction(BookRentTransaction bookRentTransaction) {
        BookRentTransaction updatedBookRentTransaction = bookRentTransactionRepository.save(bookRentTransaction);
        return updatedBookRentTransaction;
    }

    @Override
    public List<BookRentTransaction> checkTransactionByBookIdAndUserId(int bookId, int userId) {
        List<BookRentTransaction> bookRentTransaction = bookRentTransactionRepository.findByBookIdAndUserId(bookId,userId);
        return bookRentTransaction;
    }

    @Override
    public List<BookRentTransaction> getBookTransactionsByUserId(int userId) {
        List<BookRentTransaction> bookRentTransactions = bookRentTransactionRepository.findByUserId(userId);
        return bookRentTransactions;
    }

    @Override
    public List<BookRentTransaction> getBookTransactionsByBookId(int bookId) {
        return bookRentTransactionRepository.findByBookId(bookId);
    }

    @Override
    public BookRentTransaction getTransactionByBookIdAndTansactionId(int bookId, UUID transactionId) {
        BookRentTransaction bookRentTransaction = bookRentTransactionRepository.findByBookIdAndTransactionId(bookId,
                transactionId);
        return bookRentTransaction;
    }
}
