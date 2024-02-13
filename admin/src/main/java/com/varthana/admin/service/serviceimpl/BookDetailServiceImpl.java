package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.repository.BookDetailRepository;
import com.varthana.admin.service.BookDetailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDetailServiceImpl implements BookDetailService {
    @Autowired
    private BookDetailRepository bookDetailRepository;

    private Logger logger = LogManager.getLogger(BookDetailServiceImpl.class);

    @Override
    public BookDetail saveBook(BookDetail bookDetail) {
        try {
            BookDetail savedBook = bookDetailRepository.save(bookDetail);
            logger.warn("saveBook service {}",bookDetail.toString());
            return savedBook;
        } catch (Exception e) {
            logger.error("Error while saving book : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public BookDetail updateBook(BookDetail bookDetail) {
        try {
            BookDetail updatedBook = bookDetailRepository.save(bookDetail);
            logger.warn("updateBook service {}",bookDetail.toString());
            return updatedBook;
        } catch (Exception e) {
            logger.error("Error while updating book : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public BookDetail deleteBook(BookDetail bookDetail) {
        try {
            logger.warn("deleteBook service {}",bookDetail.toString());
            return null;
        }catch (Exception e){
            logger.error("Error while deleting book : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public List<BookDetail> getAllBooks() {
        try {
            List<BookDetail> books = bookDetailRepository.findAll();
            logger.warn("getAllBooks service");
            return books;
        } catch (Exception e) {
            logger.error("Error while getting all books : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public BookDetail getBookById(int id) {
        try {
            if (!bookDetailRepository.existsById(id)) {
                logger.warn("getBookById service id not found {}",id);
                return null;
            }
            logger.warn("getBookById service {}",id);
            return bookDetailRepository.findById(id).get();
        } catch (Exception e) {
            logger.error("Error while getting book by id : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteBookById(int id) {
        try {
            bookDetailRepository.deleteById(id);
            logger.warn("deleteBookById service {}",id);
        } catch (Exception e) {
            logger.error("Error while deleting book by id : {}",e.getMessage());
            return;
        }
    }

    @Override
    public boolean isPresentById(int id) {
        try {
            if (bookDetailRepository.existsById(id)) {
                logger.warn("isPresentById service id not found {}",id);
                return true;
            }
            logger.warn("isPresentById service {}",id);
            return false;
        } catch (Exception e) {
            logger.error("Error while checking if book exists by id : {}",e.getMessage());
            return false;
        }
    }
}
