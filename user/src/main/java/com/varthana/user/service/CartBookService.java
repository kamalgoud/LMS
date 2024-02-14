package com.varthana.user.service;

import com.varthana.user.entity.CartBook;
import com.varthana.user.exception.CustomException;

public interface CartBookService {
    public CartBook saveCart(CartBook cart) throws CustomException;

    public void deleteCart(CartBook cart) throws CustomException;

    public CartBook updateCart(CartBook cart) throws CustomException;

    public CartBook getCartBookByBookIdAndUserId(Integer bookId, Integer userId) throws CustomException;
}
