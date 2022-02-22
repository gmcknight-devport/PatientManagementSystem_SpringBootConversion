package com.gmck.PatientManagementSystem.UserModel.Entities;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Base abstract super class for all user objects. 
 * Creates common variables for all classes, enforces validation, and
 * provides methods for each implementing class to use. 
 * @author Glenn McKnight
 *
 */
@MappedSuperclass
public abstract class User {
	
	@Id
	@NotBlank
	@Pattern(regexp = "^[ADPST][0-9][0-9][0-9][0-9]$")
	@Column(name = "USER_ID")
	private String userId;	
	@NotNull
    private char[] userPassword;
	@NotBlank
    private String title;
	@NotBlank
    private String forename;
	@NotBlank
    private String surname;
    
    protected User() {}
    
    protected User(String userId, char[] userPassword, String title, String forename, String surname) {
    	this.userId = userId;
    	this.userPassword = userPassword;
    	this.title =  title;
    	this.forename = forename;
    	this.surname = surname;
    }

    /**
     * Authentication method to verify if a username and password
     * are correct. 
     * @param userId - userId to check against entity value.
     * @param password - password to check against entity value.
     * @return boolean - true if parameters match entity values. 
     */
    public boolean authenticate(String userId, char[] password) {
    	if(userId == this.userId && Arrays.equals(password, this.userPassword)) {
    		return true;
    	}
    	return false;
    }
    
	public String getUserId() {
		return userId;
	}

	protected void setUserId(String userId) {
		this.userId = userId;
	}

	protected char[] getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(char[] userPassword) {
		this.userPassword = userPassword;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}        
}
