package com.societegenerale.accounts.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Account extends AbstractEntity{

	private static final long serialVersionUID = 669043796180473035L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
	@SequenceGenerator(name = "account_generator", sequenceName = "account_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	@JsonIgnore
	private Long id;
    @Column(name = "account_number", unique = true, nullable = false)
    private Long accountNumber;
    @Column(name = "iban", unique = true, nullable = false)
    private String iban;
    @Column(name = "rib",unique = true, nullable = false)
    private String rib;
    @Column(nullable = false)
    private BigDecimal accountPosition;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_fk", referencedColumnName = "id")
    private Client client;
	
}
