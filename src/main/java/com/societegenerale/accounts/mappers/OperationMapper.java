package com.societegenerale.accounts.mappers;

import com.societegenerale.accounts.dtos.OperationDto;
import com.societegenerale.accounts.entities.Operation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationsMapper {

    public OperationDto mapOperation(Operation operation) {
        return OperationDto.builder().id(operation.getId())
                .date(operation.getDate())
                .operationType(operation.getOperationType().toString())
                .operationAmount(operation.getOperationAmount())
                .balance(operation.getBalance())
                .build();
    }

    public List<OperationDto> mapOperations(List<Operation> operations) {
        return operations.stream().map(this::mapOperation).collect(Collectors.toList());
    }
}
