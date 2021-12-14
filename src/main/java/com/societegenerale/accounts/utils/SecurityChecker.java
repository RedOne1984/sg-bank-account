package com.societegenerale.accounts.utils;

import com.societegenerale.accounts.entities.Account;
import com.societegenerale.accounts.security.CustomUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SecurityChecker {
    public Boolean checkConnectedClientAccount(Account account, Authentication authentication) {
        Boolean isAuthorized = Boolean.FALSE;
        if(authentication.getPrincipal() instanceof CustomUserDetail && ((CustomUserDetail)authentication.getPrincipal()).getId() != null
                && account.getClient().getId().equals(((CustomUserDetail)authentication.getPrincipal()).getId())) {
            isAuthorized = Boolean.TRUE;
        }
        return isAuthorized;
    }
}
