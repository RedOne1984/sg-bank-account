package com.societegenerale.accounts.repository;

import java.util.List;

import com.societegenerale.accounts.entities.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long>{

	List<Operation> findByAccountAccountNumber(Long accountNumber, Pageable pageable);
	
	
}
