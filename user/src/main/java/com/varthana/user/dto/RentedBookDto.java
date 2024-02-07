package com.varthana.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentedBookDto {
    private int id;
    private String name;
    private String author;
    private String rentAmount;
    private String fineAmount;
    private boolean isReturned;
}
