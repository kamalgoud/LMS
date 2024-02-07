package com.varthana.admin.service;

import com.varthana.admin.entity.BookRentTransaction;

public interface BookRentTransactionService {
    public BookRentTransaction saveRentTransaction(BookRentTransaction bookRentTransaction);
    public BookRentTransaction updateRentTransaction(BookRentTransaction bookRentTransaction);
}
