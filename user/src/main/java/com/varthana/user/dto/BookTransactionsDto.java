package com.varthana.user.dto;

import java.time.LocalDate;

import com.varthana.user.enums.Transaction;

public class BookTransactionsDto {
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private String bookName;
    private Transaction type;
    private LocalDate transactionDate;
    private Double price;
    private Double rentAmount;
    private Double amountPaid;
    private Double fine;
    private Long purchasedQuantity;
}
