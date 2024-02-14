package com.varthana.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseBookRequestDto {
    private Integer bookId;
    private Integer userId;
    private String userName;
    private Long quantity;
    private Boolean isEliteUser;
}
