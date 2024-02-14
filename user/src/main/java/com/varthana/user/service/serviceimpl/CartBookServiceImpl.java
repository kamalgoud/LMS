package com.varthana.user.service.serviceimpl;

import com.varthana.user.entity.CartBook;
import com.varthana.user.exception.CustomException;
import com.varthana.user.repository.CartBookRepository;
import com.varthana.user.service.CartBookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartBookServiceImpl implements CartBookService {
    @Autowired
    private CartBookRepository cartRepository;

    private Logger logger = LogManager.getLogger(CartBookServiceImpl.class);

    @Override
    public CartBook saveCart(CartBook cart) throws CustomException {
        try {
            logger.warn("saveCart service {}", cart.toString());
            return cartRepository.save(cart);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException("Error while saving cart book");
        }
    }

    @Override
    public void deleteCart(CartBook cart) throws CustomException {
        try {
            cartRepository.delete(cart);
            logger.warn("deleteCart service {}", cart.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException("Error while deleting cart book");
        }
    }

    @Override
    public CartBook updateCart(CartBook cart) throws CustomException {
        try {
            logger.warn("updateCart service {}", cart.toString());
            return cartRepository.save(cart);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException("Error while updating cart book");
        }
    }

    @Override
    public CartBook getCartBookByBookIdAndUserId(Integer bookId, Integer userId) throws CustomException {
        try {
            logger.warn("getCartBookByBookIdAndUserId service {}", bookId);
            return cartRepository.findByBookIdAndUserId(bookId, userId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException("Error while getting cart book by book id and user id");
        }
    }
}
