package com.varthana.admin.service;

import com.varthana.admin.entity.BookRentTransaction;
import com.varthana.admin.exception.CustomException;

import java.util.List;
import java.util.UUID;

public interface BookRentTransactionService {
    public BookRentTransaction saveRentTransaction(BookRentTransaction bookRentTransaction) throws CustomException;

    public BookRentTransaction updateRentTransaction(BookRentTransaction bookRentTransaction) throws CustomException;

    public List<BookRentTransaction> checkTransactionByBookIdAndUserId(Integer bookId, Integer userId) throws CustomException;

    public List<BookRentTransaction> getBookTransactionsByUserId(Integer userId) throws CustomException;

    public List<BookRentTransaction> getBookTransactionsByBookId(Integer bookId) throws CustomException;

    public BookRentTransaction getTransactionByBookIdAndTansactionId(Integer bookId, UUID transactionId) throws CustomException;

    public List<BookRentTransaction> getAllRentalTransactions() throws CustomException;
}
