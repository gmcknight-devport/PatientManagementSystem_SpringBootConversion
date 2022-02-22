package com.gmck.PatientManagementSystem.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.ErrorUpdate.IErrorUpdateObserver;
import com.gmck.PatientManagementSystem.LoginServices.LoginService;
import com.gmck.PatientManagementSystem.LoginServices.StrategySelectFactory;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Services.TempUserService;
import com.gmck.PatientManagementSystem.Views.LoginView;
import com.gmck.PatientManagementSystem.util.EnumLookup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.UIManager;

/**
 * Controller for the LoginView and related services. 
 * Creates and sets up the LoginView and responds to user input to carry 
 * out the required operations. Has inner classes to listen for actions. 
 * @author Glenn McKnight
 *
 */
@Controller
public class LoginController implements IController, IErrorUpdateObserver {

	private LoginView view;
	private String loggedInUser;
	private LoginService loginService;
	private StrategySelectFactory stratSelect;
	private TempUserService tempService;
	
	@Autowired
	public LoginController(LoginService loginService, StrategySelectFactory stratSelect, TempUserService tempService) {
		this.loginService = loginService;
		this.stratSelect = stratSelect;
		this.tempService = tempService;
	}
	
	/**
	 * A PostConstruct method used to initialise the class when the main 
	 * method starts the application. Calls setView() and adds itself
	 * to the Error observer. 
	 */
	@PostConstruct
	public void init() {
		setView();
		ErrorUpdate.getInstance().addObserver(this);
	}
	
	@Override
	public void setView() {
		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.view = new LoginView();
		view.setVisible(true);
		
		addButtonHandler();
	}

	@Override
	public void setLoggedInUser(String userId) {
		this.loggedInUser = userId;		
		System.out.println(loggedInUser);
	}

	@Override
	public void disposeView() {
		view.dispose();	
		view = null;
	}

	@Override
	public void addButtonHandler() {
		view.addLoginButtonHandler(new LoginListener());
        view.addSignupButtonHandler(new SignupListener());
	}
	
	@Override
    public void update(String errorMessage) {
        view.displayMessage(errorMessage);
    }
	////////////////////////////////////////////////////////////////////////////
	//Inner listener classes 	
	/**
	 * Inner listening class for the login button of the view. 
	 * Ensures the login and password fields both contain values, authenticates
	 * the user, gets the UserType to ascertain the login strategy to call, 
	 * executes the login method for that UserType, sets loggedInUser, and
	 * disposes of the view.
	 * @author Glenn McKnight
	 *
	 */
    class LoginListener implements ActionListener{
        String userId;
        char[] password;
        UserType type;
        
        @Override
        public void actionPerformed(ActionEvent e) {
        	//Get values from view
        	userId = view.getUserID();
            password = view.getLoginPassword();
            
        	if(userId.length() > 0 && password.length > 0) {	        	
	            
	            //Authenticate user
	            //else is handled automatically by update observer method
	            if(loginService.authenticateUser(userId, password)) {
	            	
	            	//Get type of strategy, view, and controller to be used
	            	type = EnumLookup.lookup(UserType.class, userId.substring(0, 1));
	            	
	            	//setStrategy
	            	stratSelect.execute(type, userId);
	            	
	                //set loggedInUserId
	            	loggedInUser = userId;
	            	
	            	//Close login view
	            	disposeView();
	            }    
        	}else {
        		view.displayMessage("Enter a value for username and password");
        	}
        }
    }
    
    /**
     * Inner listening class for the signup button of the view. 
     * Gets the sign up field values, and adds a new temporary user. 
     * Calls clear fields method to update the view and displays a message 
     * to confirm the result for the user. 
     * @author Glenn McKnight
     *
     */
    class SignupListener implements ActionListener{

        /**
         *
         * @param e actionEvent - signup button click
         */
        @Override
        public void actionPerformed(ActionEvent e) {
                        
            try{    
                tempService.addUser(view.getSignupPassword(), view.getUserTitle(),
                        view.getForename(), view.getSurname(), view.getAge(),
                        view.getAddressLine1(), view.getAddressLine2(), view.getCity(), view.getPostcode());
                
                view.displayMessage("Signup request received");                
                view.clearFields();
                
            }catch(NullPointerException ex){                
                view.displayMessage("Please enter a value in each field");
            }
        }        
    }    
}
