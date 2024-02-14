package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookPurchaseTransaction;
import com.varthana.admin.exception.CustomException;
import com.varthana.admin.repository.BookPurchaseTransactionRepository;
import com.varthana.admin.service.BookPurchaseTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookPurchaseTransactionServiceImpl implements BookPurchaseTransactionService {
    @Autowired
    private BookPurchaseTransactionRepository bookPurchaseTransactionRepository;

    private Logger logger = LogManager.getLogger(BookPurchaseTransactionServiceImpl.class);

    @Override
    public BookPurchaseTransaction savePurchaseTransaction(BookPurchaseTransaction bookPurchaseTransaction)
            throws CustomException {
        try {
            logger.warn("savePurchaseTransaction service {}",bookPurchaseTransaction.toString());
            return bookPurchaseTransactionRepository.save(bookPurchaseTransaction);
        } catch (Exception e) {
            logger.error("Error while saving purchase transaction : {}",e.getMessage());
            throw new CustomException("Error while saving purchase transaction "+e.getMessage());
        }
    }

    @Override
    public List<BookPurchaseTransaction> getPurchaseTransactionsByUserId(Integer userId) throws CustomException {
        try {
            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionRepository
                    .findByUserId(userId);
            logger.warn("getPurchaseTransactionsByUserId service {}",userId);
            return bookPurchaseTransactions;
        } catch (Exception e) {
            logger.error("Error while getting purchased transactions by user id : {}",e.getMessage());
            throw new CustomException("Error while accessing purchase transaction by user id "+e.getMessage());
        }
    }

    @Override
    public List<BookPurchaseTransaction> getPurchaseTransactionsByBookId(Integer bookId) throws CustomException {
        try {
            List<BookPurchaseTransaction> bookPurchaseTransactions = bookPurchaseTransactionRepository
                    .findByBookId(bookId);
            logger.warn("getPurchaseTransactionsByBookId service {}",bookId);
            return bookPurchaseTransactions;
        } catch (Exception e) {
            logger.error("Error while getting purchased transactions by book id : {}",e.getMessage());
            throw new CustomException("Error while accessing purchase transaction by book id "+e.getMessage());
        }
    }
}
