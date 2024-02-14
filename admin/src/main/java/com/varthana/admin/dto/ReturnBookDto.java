package com.varthana.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnBookDto {
    private Integer bookId;
    private UUID transactionId;
    private Boolean isEliteUser;
}
