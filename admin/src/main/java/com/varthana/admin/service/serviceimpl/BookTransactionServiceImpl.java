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
        return bookTransactionRepository.save(bookTransaction);
    }

    @Override
    public List<BookTransaction> getTransactionsByUserId( int userId) {
        return bookTransactionRepository.findByUserId(userId);
    }
}
