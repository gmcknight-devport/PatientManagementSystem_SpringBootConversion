package com.gmck.PatientManagementSystem.ErrorUpdate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * The concrete implementation class of the ErrorObservable.
 * A singleton class created without using the Spring annotation, it
 * allows static calls to it to reduce the amount of boilerplate code
 * creating an instance of this in each class. 
 * @author Glenn McKnight
 *
 */
@Service
@Scope("Singleton")
public final class ErrorUpdate implements IErrorUpdateObservable {

	private static volatile ErrorUpdate updateInstance;   
	private List<IErrorUpdateObserver> errorObservers = new ArrayList<>();
	
	/**
     * Private constructor throws exception if a second instance instantiation is 
     * attempted.
     */
    private ErrorUpdate(){
        if (updateInstance != null){
            throw new RuntimeException("This class is a singleton, use getInstance() to access");
        }
    }
    
    /**
     * Static access method to return singleton of this class. Creates new instance
     * if one doesn't already exist.
     * @return instance of this class
     */
    public static ErrorUpdate getInstance(){
        if(updateInstance == null){
            synchronized (ErrorUpdate.class){
                updateInstance = new ErrorUpdate();
            }            
        }        
        return updateInstance;
    }
    
	@Override
	public void addObserver(IErrorUpdateObserver observer) {
		if(!errorObservers.contains(observer)){
            errorObservers.add(observer);
        }		
	}
	
	@Override
	public void removeObserver(IErrorUpdateObserver observer) {		
		int index = errorObservers.indexOf(observer);
	    if (index >= 0) errorObservers.remove(index);		
	}

	@Override
	public void removeAllObservers() {
		errorObservers.removeAll(getObservers());		
	}
	
	@Override
	public void updateObserver(String errorMessage) {
		errorObservers.forEach((signupObserver) -> {
            signupObserver.update(errorMessage);
        });		
	}
	
	/**
	 * Getter for a list of observers.
	 * @return list - of observers. 
	 */
	List<IErrorUpdateObserver> getObservers(){
		return errorObservers;
	}	
}
