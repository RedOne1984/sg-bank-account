package com.societegenerale.accounts.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Client extends AbstractEntity{

	private static final long serialVersionUID = 134545364564L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_generator")
	@SequenceGenerator(name = "client_generator", sequenceName = "client_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	@JsonIgnore
	private Long id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "client_number")
	private String clientNumber;
	@Column(name = "user_name", unique = true)
	private String userName;
	@Column(name = "email", unique = true)
	private String email;
	@JsonIgnore
	@Column(name = "password")
	private String password;
    @Column(name = "role")
    private String role;

}
