package com.varthana.admin.repository;

import com.varthana.admin.entity.BookRentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRentTransactionRepository extends JpaRepository<BookRentTransaction,Integer> {
}
