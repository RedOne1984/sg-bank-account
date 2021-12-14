package com.societegenerale.accounts.exceptions;

public class AccountException extends Exception{

    private static final long serialVersionUID = 1L;

    public AccountException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
