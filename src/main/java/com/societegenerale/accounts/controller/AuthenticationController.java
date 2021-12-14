package com.societegenerale.accounts.controller;

import javax.validation.Valid;

import com.societegenerale.accounts.security.AccessTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.societegenerale.accounts.dtos.Credentials;
import com.societegenerale.accounts.dtos.TokenInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

	private final AccessTokenProvider tokenProvider;
	
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AccessTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Operation(
            summary = "create access token using authentication credentials",
            description = "Authentication and creating access token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "authentication success"),
                    @ApiResponse(responseCode = "401", description = "authentication failed")
            })
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TokenInfo> authenticate(@Valid @RequestBody Credentials credentials) {

    	UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getPassword());
    	Authentication authentication =  authenticationManager.authenticate(userAuthToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return ResponseEntity.ok(tokenProvider.createToken(authentication));
    }
}
