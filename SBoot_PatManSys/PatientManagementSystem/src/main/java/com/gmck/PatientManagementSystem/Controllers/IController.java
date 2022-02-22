package com.gmck.PatientManagementSystem.Controllers;

/**
 * Interface to enforce the implementation of common methods for all controller classes
 * to ensure proper working of each. 
 * @author Glenn McKnight
 *
 */
public interface IController {
	
	/**
	 * Sets all the GUI components for the current view. 
	 * Sets look and fell of UIManager. Creates new View to correspond to controller 
	 * and assigns to variable. Calls adButtonHandler() and all methods to set components 
	 * on the view and populate with required data. 
	 */
	abstract void setView();
	
	/**
	 * Assign the loggedInUser ID to use for operations throughout class. 
	 * @param userId - currently logged in userID.
	 */
	abstract void setLoggedInUser(String userId);
	
	/**
	 * Dispose of view when class is being closed and set variable to null. 
	 */
	abstract void disposeView();
	
	/**
	 * Add button handler inner classes to the GUI view for each button and 
	 * operation requiring a listener class.
	 */
	abstract void addButtonHandler();
}
