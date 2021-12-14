package com.societegenerale.accounts.service;

import com.societegenerale.accounts.entities.Account;

public interface AccountService {
	Account updateAccountPosition(Account account);

	Account findAccountByAccountNumber(Long accountNumber);
}
