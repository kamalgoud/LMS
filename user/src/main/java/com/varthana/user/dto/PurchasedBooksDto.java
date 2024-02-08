package com.varthana.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedBooksDto {
    private int id;
    private int bookId;
    private int userId;
    private String bookName;
    private double amountPaid;
    private long quantity;
    private LocalDate purchasedDate;
}
