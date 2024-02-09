package com.varthana.admin.repository;

import com.varthana.admin.entity.BookPurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookPurchaseTransactionRepository extends JpaRepository<BookPurchaseTransaction,Integer> {
    List<BookPurchaseTransaction> findByUserId(int userId);
    List<BookPurchaseTransaction> findByBookId(int bookId);
}
