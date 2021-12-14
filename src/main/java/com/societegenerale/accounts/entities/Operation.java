package com.societegenerale.accounts.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.societegenerale.accounts.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operation")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Operation extends AbstractEntity{

	private static final long serialVersionUID = 1238730373805198673L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operation_generator")
	@SequenceGenerator(name = "operation_generator", sequenceName = "operation_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	@JsonIgnore
	private Long id;
    @Column(name = "operation_date")
    private Date date;
    @Column(name = "operation_amount")
    private BigDecimal operationAmount;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "operation_type")
	@Enumerated(value = EnumType.STRING)
    private OperationType operationType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "account_fk", referencedColumnName = "id")
    private Account account;

}
