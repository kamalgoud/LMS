package com.varthana.admin.repository;

import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.entity.BookRentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookRentTransactionRepository extends JpaRepository<BookRentTransaction, Integer> {
    List<BookRentTransaction> findByBookIdAndUserId(Integer bookId, Integer userId);

    List<BookRentTransaction> findByUserId(Integer userId);

    List<BookRentTransaction> findByBookId(Integer bookId);

    BookRentTransaction findByBookIdAndTransactionId(Integer bookId, UUID transactionId);
}
