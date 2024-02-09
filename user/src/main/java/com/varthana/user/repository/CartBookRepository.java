package com.varthana.user.repository;

import com.varthana.user.entity.CartBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartBookRepository extends JpaRepository<CartBook, Integer> {
    CartBook findByBookIdAndUserId(int bookId, int userId);
}
