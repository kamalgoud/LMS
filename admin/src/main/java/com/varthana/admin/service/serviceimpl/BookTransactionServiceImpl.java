package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookTransaction;
import com.varthana.admin.exception.CustomException;
import com.varthana.admin.repository.BookTransactionRepository;
import com.varthana.admin.service.BookTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookTransactionServiceImpl implements BookTransactionService {
    @Autowired
    private BookTransactionRepository bookTransactionRepository;

    private Logger logger = LogManager.getLogger(BookTransactionServiceImpl.class);

    @Override
    public BookTransaction saveTransaction(BookTransaction bookTransaction) throws CustomException {
        try {
            logger.warn("saveTransaction service {}",bookTransaction.toString());
            return bookTransactionRepository.save(bookTransaction);
        } catch (Exception e) {
            logger.error("Error while saving transaction : {}",e.getMessage());
            throw new CustomException("Error while saving transaction "+e.getMessage());
        }
    }

    @Override
    public List<BookTransaction> getTransactionsByUserId(Integer userId) throws CustomException {
        try {
            logger.warn("getTransactionsByUserId service {}",userId);
            return bookTransactionRepository.findByUserId(userId);
        } catch (Exception e) {
            logger.error("Error while getting transaction by user id : {}",e.getMessage());
            throw new CustomException("Error while getting transactions by user id "+e.getMessage());
        }
    }
}
