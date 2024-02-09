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
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(CartBook cart) {
        cartRepository.delete(cart);
    }

    @Override
    public CartBook updateCart(CartBook cart) {
        return cartRepository.save(cart);
    }

    @Override
    public CartBook getCartBookByBookIdAndUserId(int bookId,int userId) {
        return cartRepository.findByBookIdAndUserId(bookId,userId);
    }
}
