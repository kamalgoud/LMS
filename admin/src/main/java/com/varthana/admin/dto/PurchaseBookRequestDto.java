package com.varthana.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseBookRequestDto {
    private int bookId;
    private int userId;
    private String userName;
    private long quantity;
    private boolean isEliteUser;
}
