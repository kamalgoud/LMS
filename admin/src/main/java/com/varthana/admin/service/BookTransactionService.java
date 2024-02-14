package com.varthana.admin.service;

import com.varthana.admin.entity.BookTransaction;
import com.varthana.admin.exception.CustomException;

import java.util.List;

public interface BookTransactionService {
    public BookTransaction saveTransaction(BookTransaction bookTransaction) throws CustomException;

    public List<BookTransaction> getTransactionsByUserId(Integer userId) throws CustomException;
}
