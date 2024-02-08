package com.varthana.admin.service;

import com.varthana.admin.entity.BookTransaction;

import java.util.List;

public interface BookTransactionService {
    public BookTransaction saveTransaction(BookTransaction bookTransaction);
    public List<BookTransaction> getTransactionsByUserId(int userId);
}
