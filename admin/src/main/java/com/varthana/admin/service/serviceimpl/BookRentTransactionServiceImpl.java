package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookRentTransaction;
import com.varthana.admin.exception.CustomException;
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
    public BookRentTransaction saveRentTransaction(BookRentTransaction bookRentTransaction) throws CustomException {
        try {
            logger.warn("saveRentTransaction service {}", bookRentTransaction.toString());
            return bookRentTransactionRepository.save(bookRentTransaction);
        } catch (Exception e) {
            logger.error("Error while saving rent transaction : {}", e.getMessage());
            throw new CustomException("Error while saving rent transaction " + e.getMessage());
        }
    }

    @Override
    public BookRentTransaction updateRentTransaction(BookRentTransaction bookRentTransaction) throws CustomException {
        try {
            BookRentTransaction updatedBookRentTransaction = bookRentTransactionRepository.save(bookRentTransaction);
            logger.warn("updateRentTransaction service {}", bookRentTransaction.toString());
            return updatedBookRentTransaction;
        } catch (Exception e) {
            logger.error("Error while updating rent transactions : {}", e.getMessage());
            throw new CustomException("Error while updating rent transaction " + e.getMessage());
        }
    }

    @Override
    public List<BookRentTransaction> checkTransactionByBookIdAndUserId(Integer bookId,
                                                                       Integer userId) throws CustomException {
        try {
            List<BookRentTransaction> bookRentTransaction = bookRentTransactionRepository.findByBookIdAndUserId(bookId,
                    userId);
            logger.warn("checkTransactionByBookIdAndUserId service {}", bookId);
            return bookRentTransaction;
        } catch (Exception e) {
            logger.error("Error while checking transactions by book id and user id : {}", e.getMessage());
            throw new CustomException("Error while checking transaction by book id and user id " + e.getMessage());
        }
    }

    @Override
    public List<BookRentTransaction> getBookTransactionsByUserId(Integer userId) throws CustomException {
        try {
            List<BookRentTransaction> bookRentTransactions = bookRentTransactionRepository.findByUserId(userId);
            logger.warn("getBookTransactionsByUserId service {}", userId);
            return bookRentTransactions;
        } catch (Exception e) {
            logger.error("Error while getting book transactions by user id : {}", e.getMessage());
            throw new CustomException("Error while getting book transactions by user id " + e.getMessage());
        }
    }

    @Override
    public List<BookRentTransaction> getBookTransactionsByBookId(Integer bookId) throws CustomException {
        try {
            return bookRentTransactionRepository.findByBookId(bookId);
        } catch (Exception e) {
            logger.error("Error while getting book transactions by book id : {}", e.getMessage());
            throw new CustomException("Error while accessing transactions by book id " + e.getMessage());
        }
    }

    @Override
    public BookRentTransaction getTransactionByBookIdAndTansactionId(Integer bookId,
                                                                     UUID transactionId) throws CustomException {
        try {
            BookRentTransaction bookRentTransaction = bookRentTransactionRepository.findByBookIdAndTransactionId(bookId,
                    transactionId);
            return bookRentTransaction;
        } catch (Exception e) {
            logger.error("Error while getting transactions : {}", e.getMessage());
            throw new CustomException("Error while accessing transactions by book and transaction ids " +
                    e.getMessage());
        }
    }
}
