package com.gmck.PatientManagementSystem.Messaging.Services;

/**
 * Interface to enforce implementation of methods required by any class 
 * displaying user messages.
 * @author Glenn McKnight
 *
 */
public interface IDisplayMessage {
	/**
	 * Fetches messages from the specific user repository, converts into 
	 * required format to be passed to a GUI object and displayed
	 */
	public void setUserMessages();
}
