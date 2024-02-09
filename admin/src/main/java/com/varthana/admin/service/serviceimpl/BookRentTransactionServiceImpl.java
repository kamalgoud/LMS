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
        try {
            return bookRentTransactionRepository.save(bookRentTransaction);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BookRentTransaction updateRentTransaction(BookRentTransaction bookRentTransaction) {
        try {
            BookRentTransaction updatedBookRentTransaction = bookRentTransactionRepository.save(bookRentTransaction);
            return updatedBookRentTransaction;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<BookRentTransaction> checkTransactionByBookIdAndUserId(int bookId, int userId) {
        try {
            List<BookRentTransaction> bookRentTransaction = bookRentTransactionRepository.findByBookIdAndUserId(bookId, userId);
            return bookRentTransaction;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<BookRentTransaction> getBookTransactionsByUserId(int userId) {
        try {
            List<BookRentTransaction> bookRentTransactions = bookRentTransactionRepository.findByUserId(userId);
            return bookRentTransactions;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<BookRentTransaction> getBookTransactionsByBookId(int bookId) {
        try {
            return bookRentTransactionRepository.findByBookId(bookId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BookRentTransaction getTransactionByBookIdAndTansactionId(int bookId, UUID transactionId) {
        try {
            BookRentTransaction bookRentTransaction = bookRentTransactionRepository.findByBookIdAndTransactionId(bookId,
                    transactionId);
            return bookRentTransaction;
        } catch (Exception e) {
            return null;
        }
    }
}
