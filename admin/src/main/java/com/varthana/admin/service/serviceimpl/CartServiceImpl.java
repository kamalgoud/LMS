package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.Cart;
import com.varthana.admin.repository.CartRepository;
import com.varthana.admin.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Override
    public Cart saveCart(Cart cart) {
        Cart savedCart = cartRepository.save(cart);
        return savedCart;
    }

    @Override
    public Cart getCartByBookIdAndUserId(int bookId, int userId) {
        Cart cart = cartRepository.findByBookIdAndUserId(bookId,userId);
        return cart;
    }

    @Override
    public List<Cart> getCartByUserId(int userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void deleteFromCart(Cart cart) {
        cartRepository.delete(cart);
    }

    @Override
    public void deleteCartByBookIdAndUserId(int bookId, int userId) {
        cartRepository.deleteByBookIdAndUserId(bookId,userId);
    }
}
