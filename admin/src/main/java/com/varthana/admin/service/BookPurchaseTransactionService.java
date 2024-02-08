package com.varthana.admin.service;

import com.varthana.admin.entity.BookPurchaseTransaction;

import java.util.List;

public interface BookPurchaseTransactionService {
    public BookPurchaseTransaction savePurchaseTransaction(BookPurchaseTransaction bookPurchaseTransaction);
    public List<BookPurchaseTransaction> getPurchaseTransactionsByUserId(int userId);
}
