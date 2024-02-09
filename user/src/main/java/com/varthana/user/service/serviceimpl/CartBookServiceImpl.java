package com.varthana.user.service.serviceimpl;

import com.varthana.user.entity.CartBook;
import com.varthana.user.repository.CartBookRepository;
import com.varthana.user.service.CartBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartBookServiceImpl implements CartBookService {
    @Autowired
    private CartBookRepository cartRepository;

    @Override
    public CartBook saveCart(CartBook cart) {
        try {
            return cartRepository.save(cart);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteCart(CartBook cart) {
        try {
            cartRepository.delete(cart);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public CartBook updateCart(CartBook cart) {
        try {
            return cartRepository.save(cart);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CartBook getCartBookByBookIdAndUserId(int bookId, int userId) {
        try {
            return cartRepository.findByBookIdAndUserId(bookId, userId);
        } catch (Exception e) {
            return null;
        }
    }
}
