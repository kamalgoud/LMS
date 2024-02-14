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
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private String bookName;
    private Double amountPaid;
    private Long quantity;
    private LocalDate purchasedDate;
}
