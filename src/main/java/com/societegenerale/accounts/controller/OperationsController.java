package com.societegenerale.accounts.controller;

import java.math.BigDecimal;
import java.util.List;

import com.societegenerale.accounts.utils.AccountUtils;
import com.societegenerale.accounts.utils.SecurityChecker;
import com.societegenerale.accounts.exceptions.AccountException;
import com.societegenerale.accounts.service.AccountService;
import com.societegenerale.accounts.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.societegenerale.accounts.dtos.Request;
import com.societegenerale.accounts.dtos.Response;
import com.societegenerale.accounts.entities.Account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/operations")
public class OperationsController {

	private final AccountService accountService;
	
	private final SecurityChecker securityChecker;
	
	private final OperationService operationService;

    public OperationsController(AccountService accountService, SecurityChecker securityChecker, OperationService operationService) {
        this.accountService = accountService;
        this.securityChecker = securityChecker;
        this.operationService = operationService;
    }

    @Operation(
            summary = "create new operation to withdraw/deposit money",
            description = "Deposit or withdraw money from account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "operation successfully created"),
                    @ApiResponse(responseCode = "403", description = "No account found"),
                    @ApiResponse(responseCode = "401", description = "Not authorized to account"),
                    @ApiResponse(responseCode = "400", description = "insufficient fund"),
                    @ApiResponse(responseCode = "500", description = "unexpected exception ")
            })
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> postOperation(@Validated @RequestBody Request request, Authentication authentication)  {

        log.info("New operation request {} for Client {}", request, authentication.getName());
        Account account = accountService.findAccountByAccountNumber(request.getAccountNumber());

        if (account == null || account.getId() == null) {
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(HttpStatus.FORBIDDEN.toString(), "account number not found "));
        }
        if (!securityChecker.checkConnectedClientAccount(account, authentication)) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(HttpStatus.UNAUTHORIZED.toString(),"sorry, your are not authorized for this account"));
        }

        BigDecimal accountPosition = AccountUtils.calculateAccountPosition(account.getAccountPosition(), request.getOperationAmount());
        if (AccountUtils.isInsufficientFund(accountPosition)) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST.toString(),"sorry, your fund is insufficient"));
        }
        account.setAccountPosition(accountPosition);
        try {
        	operationService.processOperation(request, account);
		} catch (AccountException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR.toString(),"operation error : please contcat your service client"));
		}
        log.info("Operation successful for account {}", account.getAccountNumber());
        return ResponseEntity.ok(new Response(HttpStatus.OK.toString(), "your operation has been processed successfully. "));
    }
    
    @Operation(
            summary = "search operations by account number",
            description = "Search all operations by account number",
            responses = {
                    @ApiResponse(responseCode = "200", description = "search operations sucess"),
                    @ApiResponse(responseCode = "403", description = "No account found"),
                    @ApiResponse(responseCode = "401", description = "Not authorized to account")
            })
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllOperationForAClient(
                                                    @RequestParam(value = "accountNumber", required = true) Long accountNumber,
                                                    @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                    Authentication authentication)  {

        log.info("search operations for accountNumber: {}", accountNumber);
        
        Account account = accountService.findAccountByAccountNumber(accountNumber);

        if (account == null || account.getId() == null) {
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("account number not found ");
        }
        if (!securityChecker.checkConnectedClientAccount(account, authentication)) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized to this account");
        }
        List<com.societegenerale.accounts.entities.Operation> operations = operationService.getAllOperationByAccount(accountNumber, page, size);
        return ResponseEntity.ok(operations);
    }
}
