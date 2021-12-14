package com.societegenerale.accounts.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface ClientService {
	UserDetails loadUserById(Long id);
}
