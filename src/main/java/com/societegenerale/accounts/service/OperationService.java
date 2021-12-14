package com.societegenerale.accounts.service;

import java.util.List;

import com.societegenerale.accounts.dtos.Request;
import com.societegenerale.accounts.entities.Account;
import com.societegenerale.accounts.entities.Operation;
import com.societegenerale.accounts.exceptions.AccountException;

public interface OperationService {
	List<Operation> getAllOperationByAccount(Long accountNumber, int page, int size);

	Operation processOperation(Request request, Account account) throws AccountException;
}
