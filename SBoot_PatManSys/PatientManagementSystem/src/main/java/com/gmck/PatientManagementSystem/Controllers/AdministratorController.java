package com.gmck.PatientManagementSystem.Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.gmck.PatientManagementSystem.ErrorUpdate.IErrorUpdateObserver;
import com.gmck.PatientManagementSystem.LoginServices.ILogout;
import com.gmck.PatientManagementSystem.LoginServices.StrategySelectFactory;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.Messaging.Entities.AdministratorMessage;
import com.gmck.PatientManagementSystem.Messaging.Services.AdministratorMessageService;
import com.gmck.PatientManagementSystem.Messaging.Services.DoctorMessageService;
import com.gmck.PatientManagementSystem.Messaging.Services.IDisplayMessage;
import com.gmck.PatientManagementSystem.UserModel.Services.AdministratorService;
import com.gmck.PatientManagementSystem.UserModel.Services.DoctorService;
import com.gmck.PatientManagementSystem.UserModel.Services.SecretaryService;
import com.gmck.PatientManagementSystem.Views.AdminView;
import com.gmck.PatientManagementSystem.util.EnumLookup;

/**
 * Controller for the AdminView and related services. 
 * Creates and sets up the AdminView and responds to user input to carry 
 * out the required operations. Has inner classes to listen for actions. 
 * @author Glenn McKnight
 *
 */
@Controller
public class AdministratorController implements IController, IErrorUpdateObserver, ILogout, IDisplayMessage {
	
	@Pattern(regexp = "^[ADPST][0-9][0-9][0-9][0-9]$")
	private String loggedInUserId;    
    private AdminView view;	        
    private AdministratorService adminService;
    private AdministratorMessageService adminMessageService;
    private DoctorService docService;
    private SecretaryService secService;
    private DoctorMessageService docMessageService;
    @Autowired
    private StrategySelectFactory strategy;
    
    
    /**
     * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
     */
    @Autowired
    public AdministratorController(AdministratorService adminService, AdministratorMessageService adminMessageService, 
    		DoctorService docService, SecretaryService secService, DoctorMessageService docMessageService) {
    	
    	this.adminService = adminService;
    	this.adminMessageService = adminMessageService;
    	this.docService = docService;
    	this.secService = secService;
    	this.docMessageService = docMessageService;
    }
    
    @Override
	public void setView() {
    	try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		}catch(Exception e){
			e.printStackTrace();
		}
    		
    	view = new AdminView();
    	view.setVisible(true);
    	
    	setAdminInfo();
    	setUserMessages();
	    setDoctorCombo();
	    setSecCombo();
	    setAdminCombo();
	    addButtonHandler();
	    setLogoutHandler();
	    setDocRatings();
	    view.setAddressFields();		
	}	
    
	@Override
	public void setLoggedInUser(String userId) {
		this.loggedInUserId = userId;		
	} 
       
	@Override
    public void disposeView(){
        view.dispose();
        view = null;
    }
      
	@Override
    public void addButtonHandler() {
        view.addCreateUserButtonHandler(new CreateUserButtonListener());
        view.addRemoveDoctorButtonHandler(new DeleteDoctorButtonListener());
        view.addRemoveSecButtonHandler(new DeleteSecretaryButtonListener());
        view.addRemoveAdminButtonHandler(new DeleteAdminButtonListener());
        view.addDoctorRatingJComboHandler(new RatingJComboSelectionListener());
        view.addSendFeedbackJButtonHandler(new SendFeedbackButtonListener());
    }    
        
	@Override
    public void setLogoutHandler() {               
        view.addLogoutButtonHandler(new LogoutButtonListener());
    }
    
    @Override         
    public void setStrategy(){
        UserType type = UserType.L;
        strategy.execute(type, null);
    }
    
    @Override
    public void setUserMessages(){
    	try{        	
        	List<AdministratorMessage> messList = adminMessageService.getUserMessages(loggedInUserId);
            String[] colNames = {"ID", "My User ID", "Sender ID", "Sender Name", "Sent At", "Message"};
            String[][] messArr = new String[messList.size()][colNames.length];
            
            for(int i = 0; i < messList.size(); i++) {
            	messArr[i][0] = messList.get(i).getId().toString();
            	messArr[i][1] = messList.get(i).getUserId();
            	messArr[i][2] = messList.get(i).getSenderId();
            	messArr[i][3] = messList.get(i).getSenderName();
            	messArr[i][4] = messList.get(i).getSentAt().toString();
            	messArr[i][5] = messList.get(i).getMessage();
            }
                   
            view.setUserMessages(colNames, messArr);
            
        }catch(NullPointerException ex){
            System.out.println("No user messages");
        }
    }
    
    /**
     * Sets UserInfo in the view - basic user information added to String to be passed.
     */
    private void setAdminInfo(){
    	String adminInfo = adminService.getUser(loggedInUserId).getUserId() + ", " 
    			+ adminService.getUser(loggedInUserId).getTitle() + " "
    			+ adminService.getUser(loggedInUserId).getForename() + " "
    			+ adminService.getUser(loggedInUserId).getSurname();
        
    	view.setUserInfoJText(adminInfo);
    }
    
    /**
     * Sets view JCombo boxes with all doctors user info. 
     */
    private void setDoctorCombo(){
    	view.setDoctorCombo(docService.getUserInfo());        
    }

    /**
     * Sets view JCombo boxes with all secretaries user info. 
     */
    private void setSecCombo(){
    	view.setSecCombo(secService.getUserInfo());        
    }
    
    /**
     * Sets view JCombo boxes with all administrators user info. 
     */
    private void setAdminCombo(){
    	view.setAdminCombo(adminService.getUserInfo());
    }
    
    /**
     * Set view ratings for a selected doctor.
     * Get selected doctor ID from the view, get the ratings of that doctor from the service, 
     * and add those to a variable in the required format for the GUI. 
     */
    private void setDocRatings(){
        try{            
        	String docId = view.getSelectedDoctor().substring(0,5);
        	String[] ratings = new String[docService.getDoctorRatings(docId).size()];
        	
        	for(int i = 0; i < docService.getDoctorRatings(docId).size(); i++) {
        		ratings[i] = docService.getDoctorRatings(docId).get(i).toString();
        	}
        	            
            view.setDoctorRatingsJText(ratings);
            
        }catch(NullPointerException ex){
            System.out.println("This doctor has no ratings");
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //ErrorReporting Observer update methods
    @Override
    public void update(String errorMessage) {
        view.displayMessage(errorMessage);
    }      
    ////////////////////////////////////////////////////////////////////////////
    //Inner listener classes
    /**
     * Inner class that acts as a listener for the delete message button. 
     * Gets the id from the view of the message to be deleted and calls the appropriate 
     * service to delete it. Calls setUserMessages() to refresh the display. 
     * If a message isn't selected or doesn't exist it will return a message to the user. 
     * @author Glenn McKnight
     *
     */
    class DeleteMessageButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
        	try{
            	adminMessageService.deleteMessage(Long.valueOf(view.getMessageToDelete()));
                setUserMessages();
            
            }catch(NullPointerException ex){
                view.displayMessage("Please select a message to delete");
            }catch(NumberFormatException ex) {
            	view.displayMessage("Couldn't find to delete - incorrect message ID");
            }
        }        
    }
    
    /**
     * Inner class that acts as a listener for the delete doctor button. 
     * Gets the id from the view of the doctor to be deleted and calls the appropriate 
     * service to delete it. Calls setDoctorCombo() and setDocRatings() to refresh the display. 
     * If a doctor isn't selected it will return a message to the user. 
     * @author Glenn McKnight
     *
     */
    class DeleteDoctorButtonListener implements ActionListener{        
        public void actionPerformed(ActionEvent e) {
            try {
            	
            	docService.deleteUser(view.getRemoveDocValue().substring(0, 5));
                       
	            setDoctorCombo();
	            setDocRatings();
	            
	        }catch(NullPointerException ex){
	            view.displayMessage("No user selected");
	        }
	    }                  
    }
    
    /**
     * Inner class that acts as a listener for the delete secretary button. 
     * Gets the id from the view of the secretary to be deleted and calls the appropriate 
     * service to delete it. Calls setSecCombo() to refresh the display. 
     * If a secretary isn't selected it will return a message to the user. 
     * @author Glenn McKnight
     *
     */
    class DeleteSecretaryButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
        	try {
            	
            	secService.deleteUser(view.getRemoveSecValue().substring(0, 5));                       
	            setSecCombo();
	            
	        }catch(NullPointerException ex){
	            view.displayMessage("No user selected");
	        }
        }
    }
    
    /**
     * Inner class that acts as a listener for the delete admin button. 
     * Gets the id from the view of the admin to be deleted and calls the appropriate 
     * service to delete it. Calls setAdminCombo() to refresh the display. 
     * If an admin isn't selected it will return a message to the user. 
     * @author Glenn McKnight
     *
     */
    class DeleteAdminButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
        	try {
            	
            	adminService.deleteUser(view.getRemoveAdminValue().substring(0, 5));                       
	            setAdminCombo();
	            
	        }catch(NullPointerException ex){
	            view.displayMessage("No user selected");
	        }
        }
    }
    
    /**
     * Inner class that acts as a listener for the create user button. 
     * Ensures required fields are not blank, gathers the user information from the 
     * fields in the view, adds each user type to a map, and calls the appropriate method
     * stored in the map based on the userType combo box value. Then calls
     * setDoctorCombo(), setAdminCombo(), and setSecCombo() to refresh the display. Then
     * clears the user fields on the view. If the some values are null a message will
     * be displayed for the user. 
     * @author Glenn McKnight
     *
     */
    class CreateUserButtonListener implements ActionListener{

    	UserType type;
    	char[] password;
    	String title, forename, surname, addressLine1, addressLine2, city, postcode;    	
    	Map<UserType, Runnable> createMethods = new HashMap<>();
            	
    	private void init() {
    		String user = view.getUserTypeJCombo().substring(0, 1);
        	type = EnumLookup.lookup(UserType.class, user);
        	password = view.getjPasswordField();
        	title = view.getTitleJCombo().getSelectedItem().toString();
        	forename = view.getForenameJText();
        	surname = view.getSurnameJTextField();
        	addressLine1 = view.getAddressLine1Text();
        	addressLine2 = view.getAddressLine2Text();
        	city = view.getCityText();
        	postcode = view.getPostcodeText();
        	
        	createMethods.put(UserType.A, () -> adminService.addUser(password, title, forename, surname));
        	createMethods.put(UserType.D, () -> docService.addUser(password, title, forename, surname, 
        			addressLine1, addressLine2, city, postcode));
        	createMethods.put(UserType.S, () -> secService.addUser(password, title, forename, surname));
    	}
    	
        public void actionPerformed(ActionEvent e) {   
            
            if(view.getjPasswordField().length > 0 && !view.getForenameJText().isEmpty()&&
                    !view.getSurnameJTextField().isEmpty()) {                    
            	
            	init();
            	createMethods.get(type).run();
            	
                setDoctorCombo();
                setSecCombo();
                setAdminCombo();
                view.clearCreateUserFields();
            }else{
                view.displayMessage("Enter a value in each field");
            }
        }        
    }
    
    /**
     * Inner listening class to change the value of a text area on the view when 
     * a new item is selected from a drop-down menu.
     * @author Glenn McKnight
     *
     */
    class RatingJComboSelectionListener implements ActionListener{        
        public void actionPerformed(ActionEvent e) {
            setDocRatings();
        }        
    }
    
    /**
     * Inner listening class for the send feedback button. 
     * Sends feedback to a doctor based on ratings given by a user and information supplied by the admin. 
     * Gets feedback from the view, and creates a message to send to the specific doctor. 
     * Clears the text from the feedback text field. 
     * @author Glenn McKnight
     *
     */
    class SendFeedbackButtonListener implements ActionListener{        
        public void actionPerformed(ActionEvent e) {
            String message = "You've received this feedback: " + view.getDoctorFeedback();
            String senderName = adminService.getUser(loggedInUserId).getForename() + " " 
        			+ adminService.getUser(loggedInUserId).getSurname();
            
            docMessageService.createMessage(view.getSelectedDoctor().substring(0, 5), loggedInUserId, senderName,
            		LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), message);
            
            view.clearFeedback();
        }       
    }
    
    /**
     * Inner listening class attached to the logout button.
     * Calls the setStrategy() method to logout and return to the login view, 
     * and calls disposeView(). 
     * @author Glenn McKnight
     *
     */
    class LogoutButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            setStrategy();
            disposeView();
        }        
    }	
}
