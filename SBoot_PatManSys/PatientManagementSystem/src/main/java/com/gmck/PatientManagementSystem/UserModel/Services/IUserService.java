package com.gmck.PatientManagementSystem.UserModel.Services;

import java.util.List;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.gmck.PatientManagementSystem.UserModel.UserType;

/**
 * Interface for UserService to enforce the implementation of specified methods to ensure proper 
 * working of an user service classes - allows operations to be carried out on
 * data from repository. 
 * @author Glenn McKnight
 *
 */
public interface IUserService {

	/**
	 * Get a user by the specified ID parameter.
	 * @param userId - ID of the user to return. 
	 * @return User - for specific user entity.
	 */
	Object getUser(String userId);	
	
	/**
	 * Generic getter to return all users that extend User class -
	 * specific to the implementing class. 
	 * @param <T> - the class that extends abstract User. 
	 * @param userId - the ID of the user to get messages for. 
	 * @return List - of messages for a user ID. 
	 */
	<T extends User> List<T> getAllUsers();
	
	/**
	 * Gets information about each user including ID, title, forename, and surname.
	 * @return List - of Strings with user info.
	 */
	List<String> getUserInfo();
	
	/**
	 * Delete the user specified by the parameter.
	 * Check the user exists in the repo, remove the user from the repo, 
	 * and save result. 
	 * @param userId - ID of user to be deleted. 
	 */
	void deleteUser(String userId);
	
	/**
	 * Updates the specified user with any value that hasn't been passed as null.
	 * @param userId - ID of user to be updated. 
	 * @param title - new title value, can be null.
	 * @param forename - new forename value, can be null.
	 * @param surname - new surname value, can be null.
	 */
	void updateUser(String userId, String title, String forename, String surname);	
	
	/**
	 * Changes the users password. 
	 * Checks user exists in repo, gets user, and sets a new password before
	 * saving. 
	 * @param userId - ID of user to change password for.
	 * @param newPassword - new value for the user password. 
	 */
	void changePassword(String userId, char[] newPassword);
	
	/**
	 * Get type of user this class operates on. 
	 * @return UserType - type of user. 
	 */
	UserType getType();
}