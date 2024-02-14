package com.varthana.admin.entity;

import com.varthana.admin.enums.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_transaction_id")
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private String bookName;
    @Enumerated(EnumType.STRING)
    private Transaction type;
    private LocalDate transactionDate;
    private Double price;
    private Double rentAmount;
    private Double amountPaid;
    private Double fine;
    private Long totalQuantity;
    private Long rentedQuantity;
    private Long remainingQuantity;
    private Long purchasedQuantity;
    private LocalDateTime transactionTime;
}
