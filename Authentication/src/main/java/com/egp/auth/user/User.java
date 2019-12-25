package com.egp.auth.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Data
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;
	private String password;
}
