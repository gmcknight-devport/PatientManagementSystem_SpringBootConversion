package com.gmck.PatientManagementSystem.ErrorUpdate;

/**
 * Interface to enforce implementation of specified methods
 * for the error observable.  
 * @author Glenn McKnight
 *
 */
public interface IErrorUpdateObservable {
	/**
	 * Add an observer to the stored data structure of observers. 
	 * @param observer - The class that is observing and implementing observer interface.
	 */
	public void addObserver(IErrorUpdateObserver observer);
	
	/**
	 * Remove an observer to the stored data structure of observers. 
	 * @param observer - The class that is observing and implementing observer interface.
	 */
    public void removeObserver(IErrorUpdateObserver observer);
    
    /**
	 * Remove all observers to the stored data structure of observers. 
	 */
    public void removeAllObservers();
    
    /**
     * Update all observers in the stored data structure of observers with the 
     * message parameter.
     * @param errorMessage - The message to be sent to each observer. 
     */
    public void updateObserver(String errorMessage);
}
