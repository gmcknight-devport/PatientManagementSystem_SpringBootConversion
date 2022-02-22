package com.gmck.PatientManagementSystem.LoginServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gmck.PatientManagementSystem.Controllers.LoginController;
import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;

/**
 * Implementation of the ILoginStrategy interface to set strategy for
 * any logout. 
 * @author Glenn McKnight
 *
 */
@Component
public class LogoutStrategy implements ILoginStrategy {

	private LoginController controller;
	
	/**
     * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
     */
	@Autowired
	public LogoutStrategy(LoginController controller) {
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
		return UserType.L;
	}
	
	@Override
	public void execute(String userId) {
		userId = null;
		setLoggedInUser(userId);
		setObserver();
		setView();
	}
}
