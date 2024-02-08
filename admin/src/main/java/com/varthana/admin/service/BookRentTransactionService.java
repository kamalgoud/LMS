package com.varthana.admin.service;

import com.varthana.admin.entity.BookRentTransaction;

import java.util.List;
import java.util.UUID;

public interface BookRentTransactionService {
    public BookRentTransaction saveRentTransaction(BookRentTransaction bookRentTransaction);
    public BookRentTransaction updateRentTransaction(BookRentTransaction bookRentTransaction);
    public List<BookRentTransaction> checkTransactionByBookIdAndUserId(int bookId,int userId);
    public List<BookRentTransaction> getBookTransactionsByUserId(int userId);
    public BookRentTransaction getTransactionByBookIdAndTansactionId(int bookId, UUID transactionId);
}
