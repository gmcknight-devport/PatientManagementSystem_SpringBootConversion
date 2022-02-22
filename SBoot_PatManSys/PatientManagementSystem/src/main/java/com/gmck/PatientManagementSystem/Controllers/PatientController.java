/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gmck.PatientManagementSystem.Controllers;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.gmck.PatientManagementSystem.Appointment.Appointment;
import com.gmck.PatientManagementSystem.Appointment.Services.AppointmentService;
import com.gmck.PatientManagementSystem.ErrorUpdate.IErrorUpdateObserver;
import com.gmck.PatientManagementSystem.LoginServices.ILogout;
import com.gmck.PatientManagementSystem.LoginServices.StrategySelectFactory;
import com.gmck.PatientManagementSystem.Messaging.Entities.PatientMessage;
import com.gmck.PatientManagementSystem.Messaging.Services.AdministratorMessageService;
import com.gmck.PatientManagementSystem.Messaging.Services.IDisplayMessage;
import com.gmck.PatientManagementSystem.Messaging.Services.PatientMessageService;
import com.gmck.PatientManagementSystem.Messaging.Services.SecretaryMessageService;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Services.DoctorService;
import com.gmck.PatientManagementSystem.UserModel.Services.PatientService;

import com.gmck.PatientManagementSystem.Views.PatientView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.UIManager;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * The controller for the PatientGUI and it's interaction with the UserFacade model.
 * Implements IController interface methods for setting up the controller.
 * @author Glenn McKnight
 */
@Controller
public class PatientController implements IController, IErrorUpdateObserver, ILogout, IDisplayMessage {

	@Pattern(regexp = "^[ADPST][0-9][0-9][0-9][0-9]$")
	private String loggedInUserId;	
	private PatientView view;	
	private PatientService patService;
	private PatientMessageService patMessageService;
	private AppointmentService appointService;
	private DoctorService docService;
	private SecretaryMessageService secMessageService;
	private AdministratorMessageService adminMessageService;
    private StrategySelectFactory strategy;
    
    /**
     * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
     */
    @Autowired
	public PatientController( PatientService patService, PatientMessageService patMessageService, AppointmentService appointService,
			DoctorService docService, SecretaryMessageService secMessageService, AdministratorMessageService adminMessageService,
			StrategySelectFactory strategy) {
		
		this.patService = patService;
		this.patMessageService = patMessageService;
		this.appointService = appointService;
		this.docService = docService;
		this.secMessageService = secMessageService;
		this.adminMessageService = adminMessageService;
		this.strategy = strategy;
	}
	
    /**
     * Assigns AdminGUI to the view variable
     * @param view PatientGUI view
     */
	@Override
    public void setView() {
		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		}catch(Exception e){
			e.printStackTrace();
		}
		
        view = new PatientView();
        view.setVisible(true);
        
        setLogoutHandler();
        setPatientInfo();
        setUserMessages();
        setPatientCurrPrescription();
        setPatientAppointment();
        setAppointmentDateFormat();
        setDoctorCombo();
        setAppointmentTimes();
        setRatedDoctors();
        setNotesHistory();
        setAppointmentHistory();
        setPrescriptionHistory();
        addButtonHandler();
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
        view.addDeleteMessageButtonHandler(new DeleteMessageButtonListener());
        view.addRateDoctorButtonHandler(new RateDoctorListener());
        view.addRequestAppointmentButtonHandler(new RequestAppointmentListener());
        view.addAccountTerminationButtonHandler(new RequestAccountTerminationListener());
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
        	List<PatientMessage> messList = patMessageService.getUserMessages(loggedInUserId);
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
    private void setPatientInfo(){
    	String patInfo = patService.getUser(loggedInUserId).getUserId() + ", " 
    			+ patService.getUser(loggedInUserId).getTitle() + " "
    			+ patService.getUser(loggedInUserId).getForename() + " "
    			+ patService.getUser(loggedInUserId).getSurname();
        
    	view.setUserInfoText(patInfo);
    }
        
    /**
     * Sets the current patient prescription in the view from the appropriate service. 
     */
    private void setPatientCurrPrescription(){
        try{
            view.setCurrentPrescription(patService.getCurrentPresription(loggedInUserId));
            
        }catch(NullPointerException ex){
            System.out.println("No current prescription");
        }catch(IndexOutOfBoundsException ex){
            System.out.println("Current prescription problem");
        }
    }
     
    /**
     * Sets current appointments for specific patient in the view. 
     * Gets all patient appointments from the service and adds those to a 
     * variable to be passed in the required format for the GUI. 
     */ 
    private void setPatientAppointment(){
    	try{        	
        	List<Appointment> appointList = appointService.getPatientAppointments(loggedInUserId);
            String[] colNames = {"ID", "Patient ID", "Doctor ID", "Start Time", "End Time", "Date"};
            String[][] appointArr = new String[appointList.size()][colNames.length];
            
            for(int i = 0; i < appointList.size(); i++) {
            	appointArr[i][0] = appointList.get(i).getId().toString();
            	appointArr[i][1] = appointList.get(i).getPatientId();
            	appointArr[i][2] = appointList.get(i).getDoctorId();
            	appointArr[i][3] = appointList.get(i).getStartTime().toString();
            	appointArr[i][4] = appointList.get(i).getEndTime().toString();
            	appointArr[i][5] = appointList.get(i).getAppointmentDate().toString();
            }
            
            view.setAppointmentTable(colNames, appointArr); 
            
        }catch(NullPointerException ex){
            System.out.println("User has no booked appointments");
        }
    }
    
    /**
     * Sets the appointment date spinner format in the view.
     */
    private void setAppointmentDateFormat(){
        view.setAppointmentDateSpinnerFormat();
    }
    
    /**
    * Sets possible appointment times in the view. 
    * Gets the possible times from the service and adds them to a variable
    * to be passed in the required format to the GUI. 
    */  
    private void setAppointmentTimes(){
    	List<Set<LocalTime>> timesList = appointService.getPossibleTimes();
    	List<String> times = new ArrayList<>();
    	
    	for(Set<LocalTime> t : timesList) {
    		times.add(t.iterator().next().toString());
    	}
    	
        view.setAppointmentTimeCombo(times);
    }
    
    /**
    * Sets view JCombo boxes with all doctors user info. 
    */
    private void setDoctorCombo(){
    	view.setDoctorCombos(docService.getUserInfo());        
    }
    
    /**
     * Set doctor ratings in the view. 
     */
    private void setRatedDoctors(){
        String[] docRatings = new String[docService.getUserInfo().size()];
        
        try{
            for(int i = 0; i < docRatings.length; i++){
                docRatings[i] = docService.getDoctorsWithRatings().get(i);
            }
        }catch(NullPointerException ex){
            System.out.println("No doctor ratings available");
        }
        view.setRatedDoctors(docRatings);
    }
    
    /**
     * Sets appointment history for a patient by specified user ID. 
     * Gets patient ID from the view, gets the previous patient appointments from
     * the appropriate service, and adds them to a variable to be passed
     * in the required format to the GUI. 
     */ 
    public void setAppointmentHistory(){
    	try{        	
            String[] appointHistory;
            List<Appointment> appointList = appointService.getPreviousAppointments(loggedInUserId);
            appointHistory = new String[appointList.size()];
            
            for(int i = 0; i < appointHistory.length; i++){
                appointHistory[i] = appointList.get(i).getPatientId() + ", " +
                		appointList.get(i).getDoctorId() + ", " +
                		appointList.get(i).getStartTime().toString() + ", " +
                		appointList.get(i).getEndTime().toString() + ", " +
                		appointList.get(i).getAppointmentDate().toString();
            }
            
            view.setAppointmentHistory(appointHistory);           
            
        }catch(NullPointerException ex){
            System.out.println("No patient history");
        }
    }
    
    /**
     * Sets prescription history for a patient by specified user ID. 
     * Gets patient ID from the view, gets the patient prescription history from
     * the appropriate service, and adds them to a variable to be passed
     * in the required format to the GUI. 
     */ 
    public void setPrescriptionHistory(){        
        try{
            String[] prescriptions = new String[patService.getUser(loggedInUserId).getPrescriptions().size()];
           
            for(int i = 0; i < prescriptions.length; i++){
            	prescriptions[i] = patService.getUser(loggedInUserId).getPrescriptions().get(i).getPrescription();
            }
            view.setPrescriptionHistory(prescriptions);            
            
        }catch(NullPointerException ex){
            System.out.println("User has no previous prescriptions");
        }
    }
    
    /**
     * Sets notes history for a patient by specified user ID. 
     * Gets patient ID from the view, gets the patient notes from
     * the appropriate service, and adds them to a variable to be passed
     * in the required format to the GUI. 
     */ 
    public void setNotesHistory(){
        
        try{
            String[] notes = new String[patService.getUser(loggedInUserId).getNotes().size()];
            
            for(int i = 0; i < notes.length; i++){
                notes[i] = patService.getUser(loggedInUserId).getNotes().get(i);
            }
            
            view.setNotesHistory(notes);
        }catch(NullPointerException ex){
            System.out.println("User has no notes history");
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
            	patMessageService.deleteMessage(Long.valueOf(view.getMessageToDelete()));
                setUserMessages();
            
            }catch(NullPointerException ex){
                view.displayMessage("Please select a message to delete");
            }catch(NumberFormatException ex) {
            	view.displayMessage("Couldn't find to delete - incorrect message ID");
            }
        }        
    }
    
    /**
     * Inner Listener for the request account termination button of the view. 
     * Sends a message to all secretaries requesting termination of the account 
     * for the loggedInUserId via the appropriate message service. 
     * @author Glenn McKnight
     *
     */
    class RequestAccountTerminationListener implements ActionListener{        
        public void actionPerformed(ActionEvent e) {
        	
        	String message = "Account Termination Request. ";
            String senderName = patService.getUser(loggedInUserId).getForename() + " " 
        			+ patService.getUser(loggedInUserId).getSurname();
            
            secMessageService.createMessageToAll(loggedInUserId, senderName,
            		LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), message);
        }        
    }
    
    /**
     * Inner listener class for the request appointment button of the view. 
     * Gets the required parameters for an appointment from the view and calls 
     * request appointment method of the appropriate service. 
     * @author Glenn McKnight
     *
     */
    class RequestAppointmentListener implements ActionListener{
    	LocalTime startTime;
    	
        public void actionPerformed(ActionEvent e) {
            try{
            startTime = view.getAppointmentTime();
            appointService.requestAppointment(loggedInUserId, view.getAppointmentDoctor(), 
                    startTime, startTime.plusMinutes(30), view.getAppointmentDate());
            
            }catch(IllegalArgumentException ex){
                view.displayMessage("Couldn't create appointment");
            }
            catch(DateTimeParseException ex) {
            	view.displayMessage("Couldn't get appointment date/time");
            }
        }        
    }
    
    /**
     * Inner listener class for the rate doctor button of the view. 
     * Calls add rating of the appropriate service - passing the doctor ID
     * from the view, creates a message to send to each admin to notify
     * them of the feedback for the doctor. Clears fields in the view, and 
     * calls setRatedDoctors() to update the GUI. 
     * @author Glenn McKnight
     *
     */
    class RateDoctorListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            try{
            	//Add doc rating
                docService.addRating(view.getRatedDoctor().substring(0, 5), view.getDoctorRating());
                     
                //Create Message
                String message = view.getRatedDoctor().substring(0,5) + " has received this rating: " + view.getDoctorRating();
                String senderName = patService.getUser(loggedInUserId).getForename() + " " 
            			+ patService.getUser(loggedInUserId).getSurname();
                     
                //Send feedback
                adminMessageService.createMessageToAll(loggedInUserId, senderName,
                		LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), message);
                
                //Update UI
                setRatedDoctors();
                view.clearFeedback();
            
            }catch(NullPointerException ex){
                view.displayMessage("Select a rating for a doctor");
            }
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
