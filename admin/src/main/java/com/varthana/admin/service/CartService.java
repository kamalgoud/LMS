package com.varthana.admin.service;

import com.varthana.admin.entity.Cart;

import java.util.List;

public interface CartService {
    public Cart saveCart(Cart cart);
    public Cart getCartByBookIdAndUserId(int bookId,int userId);

    public List<Cart> getCartByUserId(int userId);

    public void deleteFromCart(Cart cart);

    public void deleteCartByBookIdAndUserId(int bookId,int userId);
}
