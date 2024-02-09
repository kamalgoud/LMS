package com.varthana.admin.repository;

import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.entity.BookRentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookRentTransactionRepository extends JpaRepository<BookRentTransaction,Integer> {
    List<BookRentTransaction> findByBookIdAndUserId(int bookId,int userId);
    List<BookRentTransaction> findByUserId(int userId);
    List<BookRentTransaction> findByBookId(int bookId);
    BookRentTransaction findByBookIdAndTransactionId(int bookId, UUID transactionId);
}
