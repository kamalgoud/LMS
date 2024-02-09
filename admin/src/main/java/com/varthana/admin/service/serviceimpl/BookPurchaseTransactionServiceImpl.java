package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookPurchaseTransaction;
import com.varthana.admin.repository.BookPurchaseTransactionRepository;
import com.varthana.admin.service.BookPurchaseTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookPurchaseTransactionServiceImpl implements BookPurchaseTransactionService {
    @Autowired
    private BookPurchaseTransactionRepository bookPurchaseTransactionRepository;

    @Override
    public BookPurchaseTransaction savePurchaseTransaction(BookPurchaseTransaction bookPurchaseTransaction) {
        try {
            return bookPurchaseTransactionRepository.save(bookPurchaseTransaction);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<BookPurchaseTransaction> getPurchaseTransactionsByUserId(int userId) {
        try {
            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionRepository
                    .findByUserId(userId);
            return bookPurchaseTransactions;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<BookPurchaseTransaction> getPurchaseTransactionsByBookId(int bookId) {
        try {
            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionRepository
                    .findByBookId(bookId);
            return bookPurchaseTransactions;
        } catch (Exception e) {
            return null;
        }
    }
}
