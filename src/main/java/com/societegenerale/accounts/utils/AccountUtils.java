package com.societegenerale.accounts.utils;

import java.math.BigDecimal;

import com.societegenerale.accounts.enums.OperationType;

public abstract class AccountUtils {

	
	private AccountUtils() throws IllegalAccessException {
		throw new IllegalAccessException("static methods class not to be instanciated");
	}
	
	
	
	public static OperationType determineOperationType(BigDecimal amount) {
		if(amount != null && amount.intValue()<0) {
			return OperationType.WITHDRAWAL;
		}else {
			return OperationType.DEPOSIT;
		}
	}
	
	public static BigDecimal calculateAccountPosition(BigDecimal currentPosition, BigDecimal operationAmount) {
		return currentPosition.add(operationAmount);
	}

	public static boolean isInsufficientFund(BigDecimal amount) {
		return amount == null || amount.doubleValue() < 0;
	}
}
