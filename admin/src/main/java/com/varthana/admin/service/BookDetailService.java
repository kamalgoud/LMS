package com.varthana.admin.service;

import com.varthana.admin.entity.BookDetail;

import java.util.List;

public interface BookDetailService {
    public BookDetail saveBook(BookDetail bookDetail);

    public BookDetail updateBook(BookDetail bookDetail);

    public BookDetail deleteBook(BookDetail bookDetail);

    public List<BookDetail> getAllBooks();

    public BookDetail getBookById(int id);

    public void deleteBookById(int id);

    public boolean isPresentById(int id);
}
