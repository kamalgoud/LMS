package com.varthana.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_rent_transaction_id")
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private Double price;
    private String bookName;
    private String userName;
    private LocalDate rentedDate;
    private LocalDate expectedReturnDate;
    private LocalDate returnDate;
    private Double rentAmount;
    private Double fineAmount;
    private UUID transactionId;
    private LocalDateTime rentTransactionTime;
    private LocalDateTime returnTransactionTime;
}
