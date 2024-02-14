package com.varthana.admin.service;

import com.varthana.admin.entity.BookRentTransaction;
import com.varthana.admin.exception.CustomException;

import java.util.List;
import java.util.UUID;

public interface BookRentTransactionService {
    public BookRentTransaction saveRentTransaction(BookRentTransaction bookRentTransaction) throws CustomException;

    public BookRentTransaction updateRentTransaction(BookRentTransaction bookRentTransaction) throws CustomException;

    public List<BookRentTransaction> checkTransactionByBookIdAndUserId(int bookId, int userId) throws CustomException;

    public List<BookRentTransaction> getBookTransactionsByUserId(int userId) throws CustomException;

    public List<BookRentTransaction> getBookTransactionsByBookId(int bookId) throws CustomException;

    public BookRentTransaction getTransactionByBookIdAndTansactionId(int bookId, UUID transactionId) throws CustomException;
}
