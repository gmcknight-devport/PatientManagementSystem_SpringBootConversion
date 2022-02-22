package com.gmck.PatientManagementSystem.LoginServices;

import com.gmck.PatientManagementSystem.UserModel.UserType;

/**
 * Interface to enforce the implementation of specified methods to ensure each strategy
 * has a consistent methods and format.
 * @author Glenn McKnight
 *
 */
public interface ILoginStrategy {
	/**
	 * Sets the currently logged in user via the parameter.
	 * @param userId - the currently logged in user to be set. 
	 */
	abstract void setLoggedInUser(String userId);
	/**
	 * Adds the controller as an observer to the error observable implementation. 
	 */
	abstract void setObserver();
	/**
	 * Sets the view to be used in the controller class.
	 */
	abstract void setView(); 
	/**
	 * Getter for the UserType corresponding to this strategy. 
	 * @return UserType - of this class (first letter).
	 */
	abstract UserType getType();	
	/**
	 * Executes the strategy in the correct order and passes all required parameters. 
	 * @param userId - the ID of the newly logged in user.
	 */
	abstract void execute(String userId);
}
