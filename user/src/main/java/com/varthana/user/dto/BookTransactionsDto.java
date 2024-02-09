package com.varthana.user.dto;

import java.time.LocalDate;

import com.varthana.user.enums.Transaction;

public class BookTransactionsDto {
    private int id;
    private int bookId;
    private int userId;
    private String bookName;
    private Transaction type;
    private LocalDate transactionDate;
    private double price;
    private double rentAmount;
    private double amountPaid;
    private double fine;
}
