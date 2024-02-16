package com.varthana.admin.service;

import com.varthana.admin.entity.BookPurchaseTransaction;
import com.varthana.admin.exception.CustomException;

import java.util.List;

public interface BookPurchaseTransactionService {
    public BookPurchaseTransaction savePurchaseTransaction(BookPurchaseTransaction bookPurchaseTransaction) throws CustomException;

    public List<BookPurchaseTransaction> getPurchaseTransactionsByUserId(Integer userId) throws CustomException;

    public List<BookPurchaseTransaction> getPurchaseTransactionsByBookId(Integer bookId) throws CustomException;

    public List<BookPurchaseTransaction> getAllPurchasedBooks() throws CustomException;
}
