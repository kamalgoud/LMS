package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.repository.BookDetailRepository;
import com.varthana.admin.service.BookDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDetailServiceImpl implements BookDetailService {
    private BookDetailRepository bookDetailRepository;

    @Autowired
    public BookDetailServiceImpl(BookDetailRepository bookDetailRepository) {
        this.bookDetailRepository = bookDetailRepository;
    }

    @Override
    public BookDetail saveBook(BookDetail bookDetail) {
        try {
            BookDetail savedBook = bookDetailRepository.save(bookDetail);
            return savedBook;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BookDetail updateBook(BookDetail bookDetail) {
        try {
            BookDetail updatedBook = bookDetailRepository.save(bookDetail);
            return updatedBook;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BookDetail deleteBook(BookDetail bookDetail) {
        return null;
    }

    @Override
    public List<BookDetail> getAllBooks() {
        try {
            List<BookDetail> books = bookDetailRepository.findAll();
            return books;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BookDetail getBookById(int id) {
        try {
            if (!bookDetailRepository.existsById(id)) {
                return null;
            }
            return bookDetailRepository.findById(id).get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteBookById(int id) {
        try {
            bookDetailRepository.deleteById(id);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public boolean isPresentById(int id) {
        try {
            if (bookDetailRepository.existsById(id)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
