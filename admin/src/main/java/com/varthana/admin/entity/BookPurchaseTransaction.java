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
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private String bookName;
    private String userName;
    private Double amountPaid;
    private Long quantity;
    private LocalDate purchasedDate;
    private LocalDateTime bookPurchaseTime;
}
