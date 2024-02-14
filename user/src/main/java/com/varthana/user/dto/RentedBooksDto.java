package com.varthana.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentedBooksDto {
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private String bookName;
    private LocalDate rentedDate;
    private LocalDate expectedReturnDate;
    private LocalDate returnDate;
    private Double rentAmount;
    private Double fineAmount;
    private UUID transaction_id;
}
