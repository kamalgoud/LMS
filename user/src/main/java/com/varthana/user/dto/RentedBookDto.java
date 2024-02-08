package com.varthana.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentedBookDto {
    private int id;
    private int bookId;
    private String name;
    private String author;
    private double price;
    private double rentAmount;
    private double fineAmount;
    private boolean isReturned;
    private UUID transactionId;
}
