package com.varthana.admin.repository;

import com.varthana.admin.entity.BookTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookTransactionRepository extends JpaRepository<BookTransaction, Integer> {
    List<BookTransaction> findByUserId(Integer userId);
}
