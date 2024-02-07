package com.varthana.admin.service.serviceimpl;

import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.repository.BookDetailRepository;
import com.varthana.admin.service.BookDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookDetailServiceImpl implements BookDetailService {
    private BookDetailRepository bookDetailRepository;

    @Autowired
    public BookDetailServiceImpl(BookDetailRepository bookDetailRepository){
        this.bookDetailRepository = bookDetailRepository;
    }
    @Override
    public BookDetail saveBook(BookDetail bookDetail) {
        BookDetail savedBook = bookDetailRepository.save(bookDetail);
        return savedBook;
    }

    @Override
    public BookDetail updateBook(BookDetail bookDetail) {
        BookDetail updatedBook = bookDetailRepository.save(bookDetail);
        return updatedBook;
    }

    @Override
    public BookDetail deleteBook(BookDetail bookDetail) {
        return null;
    }

    @Override
    public List<BookDetail> getAllBooks() {
        List<BookDetail> books = bookDetailRepository.findAll();
        return books;
    }

    @Override
    public BookDetail getBookById(int id) {
        if(!bookDetailRepository.existsById(id)){
            return null;
        }
        return bookDetailRepository.findById(id).get();
    }

    @Override
    public void deleteBookById(int id) {
        bookDetailRepository.deleteById(id);
    }

    @Override
    public boolean isPresentById(int id) {
        if(bookDetailRepository.existsById(id)){
            return true;
        }
        return false;
    }
}
