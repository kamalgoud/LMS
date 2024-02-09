package com.varthana.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRentRequestDto {
    private int bookId;
    private int userId;
    private String userName;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isEliteUser;
}