package com.societegenerale.accounts.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Credentials {

	
    @NotBlank
    private String login;

    @NotBlank
    private String password;
    
}
