package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookRentTransaction;
import com.varthana.admin.repository.BookRentTransactionRepository;
import com.varthana.admin.service.BookRentTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookRentTransactionServiceImpl implements BookRentTransactionService {
    @Autowired
    private BookRentTransactionRepository bookRentTransactionRepository;

    private Logger logger = LogManager.getLogger(BookRentTransactionServiceImpl.class);

    @Override
    public BookRentTransaction saveRentTransaction(BookRentTransaction bookRentTransaction) {
        try {
            logger.warn("saveRentTransaction service {}",bookRentTransaction.toString());
            return bookRentTransactionRepository.save(bookRentTransaction);
        } catch (Exception e) {
            logger.error("Error while saving rent transaction : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public BookRentTransaction updateRentTransaction(BookRentTransaction bookRentTransaction) {
        try {
            BookRentTransaction updatedBookRentTransaction = bookRentTransactionRepository.save(bookRentTransaction);
            logger.warn("updateRentTransaction service {}",bookRentTransaction.toString());
            return updatedBookRentTransaction;
        } catch (Exception e) {
            logger.error("Error while updating rent transactions : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public List<BookRentTransaction> checkTransactionByBookIdAndUserId(int bookId, int userId) {
        try {
            List<BookRentTransaction> bookRentTransaction = bookRentTransactionRepository.findByBookIdAndUserId(bookId, userId);
            logger.warn("checkTransactionByBookIdAndUserId service {}",bookId);
            return bookRentTransaction;
        } catch (Exception e) {
            logger.error("Error while checking transactions by book id and user id : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public List<BookRentTransaction> getBookTransactionsByUserId(int userId) {
        try {
            List<BookRentTransaction> bookRentTransactions = bookRentTransactionRepository.findByUserId(userId);
            logger.warn("getBookTransactionsByUserId service {}",userId);
            return bookRentTransactions;
        } catch (Exception e) {
            logger.error("Error while getting book transactions by user id : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public List<BookRentTransaction> getBookTransactionsByBookId(int bookId) {
        try {
            return bookRentTransactionRepository.findByBookId(bookId);
        } catch (Exception e) {
            logger.error("Error while getting book transactions by book id : {}",e.getMessage());
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
            logger.error("Error while getting transactions : {}",e.getMessage());
            return null;
        }
    }
}
