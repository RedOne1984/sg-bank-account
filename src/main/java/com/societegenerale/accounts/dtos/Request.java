package com.societegenerale.accounts.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
	
    @NotNull
    private Long accountNumber;
    @NotNull
    private BigDecimal operationAmount;
}
