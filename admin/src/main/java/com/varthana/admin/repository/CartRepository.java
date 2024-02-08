package com.varthana.admin.repository;

import com.varthana.admin.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Cart findByBookIdAndUserId(int bookId,int userId);

    List<Cart> findByUserId(int userId);

    void deleteByBookIdAndUserId(int bookId,int userId);
}
