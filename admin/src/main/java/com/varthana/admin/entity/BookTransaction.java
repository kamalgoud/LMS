package com.varthana.admin.entity;

import com.varthana.admin.enums.Transaction;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
