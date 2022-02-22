package com.gmck.PatientManagementSystem.ErrorUpdate;

/**
 * Interface to enforce the implementation of specfied methods in 
 * observer classes. 
 * @author Glenn McKnight
 *
 */
public interface IErrorUpdateObserver {
	/**
	 * The method called when an observable updates its observers, 
	 * passes a message parameter and allows actions to be carried out.
	 * @param errorMessage - The message received from the observable.
	 */
	public void update(String errorMessage);
}
