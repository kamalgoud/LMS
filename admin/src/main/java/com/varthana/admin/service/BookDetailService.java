package com.varthana.admin.service;

import com.varthana.admin.entity.BookDetail;
import com.varthana.admin.exception.CustomException;

import java.util.List;

public interface BookDetailService {
    public BookDetail saveBook(BookDetail bookDetail) throws CustomException;

    public BookDetail updateBook(BookDetail bookDetail) throws CustomException;

    public List<BookDetail> getAllBooks() throws CustomException;

    public BookDetail getBookById(Integer id) throws CustomException;

    public void deleteBookById(Integer id) throws CustomException;

    public boolean isPresentById(Integer id) throws CustomException;
}
