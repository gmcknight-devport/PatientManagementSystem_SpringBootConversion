package com.gmck.PatientManagementSystem.UserModel.Entities;

import javax.persistence.Entity;

/**
 * Entity class for Administrator user. 
 * All methods taken from abstract super class - User
 * @author Glenn McKnight
 *
 */
@Entity
public class Administrator extends User {

	Administrator() {}
	
	public Administrator(String userId, char[] userPassword, String title, String forename, String surname) {
		super(userId, userPassword, title, forename, surname);
	}
}
