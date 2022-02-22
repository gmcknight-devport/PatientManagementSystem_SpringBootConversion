package com.gmck.PatientManagementSystem.LoginServices;

/**
 * Interface to enforce implementation of methods for the Logout strategy.
 * @author Glenn McKnight
 *
 */
public interface ILogout {
	/**
	 * Sets the login strategy when called. 
	 */
	abstract void setStrategy();
	/**
	 * Sets logout handler for the implementing class. 
	 */
	abstract void setLogoutHandler();
}
