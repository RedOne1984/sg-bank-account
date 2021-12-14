package com.societegenerale.accounts.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.societegenerale.accounts.entities.Account;
import com.societegenerale.accounts.service.AccountService;
import com.societegenerale.accounts.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Account updateAccountPosition(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account findAccountByAccountNumber(Long accountNumber) {
        return accountRepository.getAccountByAccountNumber(accountNumber);
    }
}
