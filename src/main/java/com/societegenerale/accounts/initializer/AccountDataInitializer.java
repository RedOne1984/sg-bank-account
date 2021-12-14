package com.societegenerale.accounts.initializer;

import java.math.BigDecimal;

import com.societegenerale.accounts.entities.Client;
import com.societegenerale.accounts.repository.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.societegenerale.accounts.entities.Account;
import com.societegenerale.accounts.repository.AccountRepository;

@Component
public class AccountDataInitializer implements CommandLineRunner {
	
	private final ClientRepository clientRepository;
	
	private final AccountRepository accountRepository;

	public AccountDataInitializer(ClientRepository clientRepository, AccountRepository accountRepository) {
		this.clientRepository = clientRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("Lorena1674");
        Client client = clientRepository.save(new Client(null, "Radouane", "Aboumehdi", "SG-304891-010984-0001", "aboumehdira", "radouane.aboumehdi@gmail.com", encodedPassword, "USER"));
        Client client2 = clientRepository.save(new Client(null, "Elon", "Musk", "SG-473674-170582-0002", "muskel", "muskel@tesla.com", encodedPassword, "USER"));
        accountRepository.save(new Account(null,11223344556L, "FR7611000035441122334455688","11000035441122334455688", new BigDecimal(0), client));
        accountRepository.save(new Account(null,33445566778L, "FR761100007853344556677888","1100007853344556677888", new BigDecimal(0), client2));
	}

}
