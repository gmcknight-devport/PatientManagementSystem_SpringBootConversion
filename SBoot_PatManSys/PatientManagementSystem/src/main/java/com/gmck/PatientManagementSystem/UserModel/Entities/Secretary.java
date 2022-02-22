package com.gmck.PatientManagementSystem.UserModel.Entities;

import javax.persistence.Entity;

/**
 * Entity class for Secretary user. 
 * All methods taken from abstract super class - User
 * @author Glenn McKnight
 *
 */
@Entity
public class Secretary extends User {

	protected Secretary() {}
	
	public Secretary(String userId, char[] userPassword, String title, String forename, String surname) {
		super(userId, userPassword, title, forename, surname);
	}
}
