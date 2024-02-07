package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookRentTransaction;
import com.varthana.admin.repository.BookRentTransactionRepository;
import com.varthana.admin.service.BookRentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
