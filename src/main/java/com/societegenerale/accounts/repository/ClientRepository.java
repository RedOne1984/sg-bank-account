package com.societegenerale.accounts.repository;

import com.societegenerale.accounts.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByUserNameOrEmail(String userName, String email);
}
