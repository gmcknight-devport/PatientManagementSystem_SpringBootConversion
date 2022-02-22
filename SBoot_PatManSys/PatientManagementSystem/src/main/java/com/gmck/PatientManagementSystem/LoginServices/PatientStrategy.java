package com.gmck.PatientManagementSystem.LoginServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gmck.PatientManagementSystem.Controllers.PatientController;
import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;

/**
 * Implementation of the ILoginStrategy interface to set strategy for
 * any patient login. 
 * @author Glenn McKnight
 *
 */
@Component
public class PatientStrategy implements ILoginStrategy {

	private PatientController controller;
	
	/**
     * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
     */
	@Autowired
	public PatientStrategy(PatientController controller) {
		this.controller = controller;
	}
	
	@Override
	public void setLoggedInUser(String userId) {
		controller.setLoggedInUser(userId);		
	}

	@Override
	public void setObserver() {
		ErrorUpdate.getInstance().removeAllObservers();
		ErrorUpdate.getInstance().addObserver(controller);
	}

	@Override
	public void setView() {
		controller.setView();
	}

	@Override
	public UserType getType() {
		return UserType.P;
	}
	
	@Override
	public void execute(String userId) {
		setLoggedInUser(userId);
		setObserver();
		setView();
	}
}
