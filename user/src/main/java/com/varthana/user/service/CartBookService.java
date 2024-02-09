package com.varthana.user.service;

import com.varthana.user.entity.CartBook;

public interface CartBookService {
    public CartBook saveCart(CartBook cart);

    public void deleteCart(CartBook cart);

    public CartBook updateCart(CartBook cart);

    public CartBook getCartBookByBookIdAndUserId(int bookId, int userId);
}
