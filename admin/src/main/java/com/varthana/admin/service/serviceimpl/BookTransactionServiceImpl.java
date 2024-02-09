package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookTransaction;
import com.varthana.admin.repository.BookTransactionRepository;
import com.varthana.admin.service.BookTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookTransactionServiceImpl implements BookTransactionService {
    @Autowired
    private BookTransactionRepository bookTransactionRepository;

    @Override
    public BookTransaction saveTransaction(BookTransaction bookTransaction) {
        try {
            return bookTransactionRepository.save(bookTransaction);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<BookTransaction> getTransactionsByUserId(int userId) {
        try {
            return bookTransactionRepository.findByUserId(userId);
        } catch (Exception e) {
            return null;
        }
    }
}
