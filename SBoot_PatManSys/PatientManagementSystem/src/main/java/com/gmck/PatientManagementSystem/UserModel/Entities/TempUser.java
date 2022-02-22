package com.gmck.PatientManagementSystem.UserModel.Entities;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

/**
 * Temporary user entity class extending the abstract User base class.
 * Corresponds to a database table and contains all information about temporary users
 * with each variables having validation annotations. 
 * @author Glenn McKnight
 *
 */
@Entity
@Validated
@Table(name = "TEMPUSER")
public class TempUser extends User {
	
	@Min(1)
	@Max(120)
	@Column(name = "AGE")
	private int age;
	
	@Embedded
	@JoinColumn(name = "USER_ID")
	Address address;
	
	@NotNull
	@Column(name = "GENDER")
	private char gender;
	
	public TempUser() {}
	
	public TempUser(String userId, char[] userPassword, String title, String forename, String surname, int age, Address address, char gender) {
		super(userId, userPassword, title, forename, surname);
		this.age = age;
		this.address = address;
		this.gender = gender;
	}

	public char[] getPassword() {
		return this.getUserPassword();
	}
	
	public int getAge() {
		return age;
	}

	public Address getAddress() {
		return address;
	}

	public char getGender() {
		return gender;
	}	
}
