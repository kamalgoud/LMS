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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookPurchaseTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_purchase_transaction_id")
    private int id;
    private int bookId;
    private int userId;
    private String bookName;
    private String userName;
    private double amountPaid;
    private long quantity;
    private LocalDate purchasedDate;
    private LocalDateTime bookPurchaseTime;

}
