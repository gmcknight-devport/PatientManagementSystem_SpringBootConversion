package com.gmck.PatientManagementSystem.Controllers;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.gmck.PatientManagementSystem.Views.DoctorView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.UIManager;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.gmck.PatientManagementSystem.Appointment.Appointment;
import com.gmck.PatientManagementSystem.Appointment.Services.AppointmentService;
import com.gmck.PatientManagementSystem.ErrorUpdate.IErrorUpdateObserver;
import com.gmck.PatientManagementSystem.LoginServices.ILogout;
import com.gmck.PatientManagementSystem.LoginServices.StrategySelectFactory;
import com.gmck.PatientManagementSystem.Medicine.Medicine;
import com.gmck.PatientManagementSystem.Medicine.Services.MedicineService;
import com.gmck.PatientManagementSystem.Messaging.Entities.DoctorMessage;
import com.gmck.PatientManagementSystem.Messaging.Services.DoctorMessageService;
import com.gmck.PatientManagementSystem.Messaging.Services.IDisplayMessage;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Services.DoctorService;
import com.gmck.PatientManagementSystem.UserModel.Services.PatientService;

/**
 * Controller for the DoctorView and related services. 
 * Creates and sets up the DoctorView and responds to user input to carry 
 * out the required operations. Has inner classes to listen for actions. 
 * @author Glenn McKnight
 *
 */
@Controller
public class DoctorController implements IController, IErrorUpdateObserver, ILogout, IDisplayMessage{

	@Pattern(regexp = "^[ADPST][0-9][0-9][0-9][0-9]$")
	private String loggedInUserId;	
	private DoctorView view;
	private DoctorService docService;
	private DoctorMessageService docMessageService;
	private AppointmentService appointService;
	private PatientService patService;
	private MedicineService medService;
    private StrategySelectFactory strategy;
	
    /** 
     * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
     */
	@Autowired
	public DoctorController(DoctorService docService, DoctorMessageService docMessageService, AppointmentService appointService,
			PatientService patService, MedicineService medService, StrategySelectFactory strategy) {
		
		this.docService = docService;
		this.docMessageService = docMessageService;
		this.appointService = appointService;
		this.patService = patService;
		this.medService = medService;
		this.strategy = strategy;
	}
	
	@Override
    public void setView() {
		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		}catch(Exception e){
			e.printStackTrace();
		}
		
        view = new DoctorView();
        view.setVisible(true);
        
        setDoctorInfo();
        setUserMessages();
        setDoctorAppointments();
        setAppointmentDateFormat();
        setAppointmentTimes();
        setPatientCombo();
        setPatientHistory();
        setPrescriptionHistory();
        setMedicinesTable();
        setLogoutHandler();
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
        view.addCreateAppointmentButtonHandler(new CreateAppointmentButtonListener());
        view.addPatientJComboListener(new PatientHistoryJComboListener());
        view.addDeleteMessageButtonHandler(new DeleteMessageButtonListener());
        view.addNotesButtonHandler(new AddNotesButtonListener());
        view.addPrescribeButtonHandler(new PrescribeButtonListener());
        view.addCreateMedicineButtonHandler(new CreateMedicineButtonListener());
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
        	List<DoctorMessage> messList = docMessageService.getUserMessages(loggedInUserId);
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
    private void setDoctorInfo(){
    	String docInfo = docService.getUser(loggedInUserId).getUserId() + ", " 
    			+ docService.getUser(loggedInUserId).getTitle() + " "
    			+ docService.getUser(loggedInUserId).getForename() + " "
    			+ docService.getUser(loggedInUserId).getSurname();
        
    	view.setUserInfoText(docInfo);
    }
            
    /**
     * Sets doctor's current appointments in the view. 
     * Gets all doctor appointments from the service and adds those to a 
     * variable to be passed in the required format for the GUI. 
     */ 
    private void setDoctorAppointments(){
        try{        	
        	List<Appointment> appointList = appointService.getDoctorAppointments(loggedInUserId);
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
     * Sets JCombo in the view with info for all patients. 
     */
    private void setPatientCombo(){
    	view.setPatientCombos(patService.getUserInfo());
    }
    
    /**
     * Sets history for a patient by specified user ID. 
     * Gets patient ID from the view, gets the patient notes from
     * the appropriate service, and adds them to a variable to be passed
     * in the required format to the GUI. 
     */ 
    private void setPatientHistory(){
        try{
        	String patId = view.getPatientHistoryID().substring(0,5);
            String[] patientNotes;
            patientNotes = new String[patService.getUser(patId).getNotes().size()];
            
            for(int i = 0; i < patientNotes.length; i++){
                patientNotes[i] = patService.getUser(patId).getNotes().get(i);
            }
            
            view.setPatientHistory(patientNotes);           
            
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
    private void setPrescriptionHistory(){
        try{
            String patientID = view.getPatientHistoryID().substring(0, 5); 
            String[] prescriptions = new String[patService.getUser(patientID).getPrescriptions().size()];

            for(int i = 0; i < prescriptions.length; i++){
                prescriptions[i] = patService.getUser(patientID).getPrescriptions().get(i).getPrescription();
            }
            view.setPrescriptionHistory(prescriptions);
            
        }catch(NullPointerException ex){
            System.out.println("No patient prescription history");
        }
    }
        
    /**
     * Sets table of medicines in the view. 
     * Gets all medicines from the service adds those to a 
     * variable to be passed in the required format for the GUI. 
     */ 
    private void setMedicinesTable(){
    	List<Medicine> medicineList = medService.getAllMedicines();
        String[] colNames = {"Name", "Dosage", "Uses", "Quantity"};
        String[][] medArr = new String[medicineList.size()][colNames.length];
        
        for(int i = 0; i < medicineList.size(); i++) {
        	medArr[i][0] = medicineList.get(i).getMedName();
        	medArr[i][1] = medicineList.get(i).getMedDosage();
        	medArr[i][2] = medicineList.get(i).getCommonUses();
        	medArr[i][3] = String.valueOf(medicineList.get(i).getQuantity());
        }
               
        view.setMedicinesTable(colNames, medArr);  
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //ErrorReporting and Messaging Observer update methods
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
            	docMessageService.deleteMessage(Long.valueOf(view.getMessageToDelete()));
                setUserMessages();
            
            }catch(NullPointerException ex){
                view.displayMessage("Please select a message to delete");
            }catch(NumberFormatException ex) {
            	view.displayMessage("Couldn't find to delete - incorrect message ID");
            }
        }        
    }
    
    /**
     * Inner listener class for create appointment button in the view. 
     * Gets all appointment information from the view, and calls create 
     * appointment method from the appropriate service. Calls setDoctorAppointments()
     * to update the view. 
     * @author Glenn McKnight
     *
     */
    class CreateAppointmentButtonListener implements ActionListener{
    	LocalTime startTime;
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            	startTime = view.getAppointmentTime();
            	appointService.createAppointment(view.getAppointmentPatient().substring(0, 5), loggedInUserId, startTime, 
            			startTime.plusMinutes(30), view.getAppointmentDate(), true);
            
            setDoctorAppointments();
            
            }catch(IllegalArgumentException ex){
                view.displayMessage("Couldn't create appointment");
            }
            catch(DateTimeParseException ex) {
            	view.displayMessage("Couldn't get appointment date/time");
            }
        }       
    }
    
    /**
     * Inner listening class to change the value of two text areas on the view when 
     * a new item is selected from a drop-down menu.
     * @author Glenn McKnight
     *
     */
    class PatientHistoryJComboListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            setPatientHistory();
            setPrescriptionHistory();
        }        
    }
    
    /**
     * Inner listening class for add notes button of the view. 
     * Gets the patient ID and the notes to be added from the view, 
     * and calls the add notes method from the appropriate service. 
     * Calls setPatientHistory() to update the view and the method to 
     * clear the fields in the GU. 
     * @author Glenn McKnight
     *
     */
    class AddNotesButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            try{
                patService.addPatientNotes(view.getPatientHistoryID().substring(0,5), 
                        view.getNotes());
                                
                setPatientHistory();
                view.clearNotesJText();
            
            }catch(NullPointerException ex){
                view.displayMessage("Please enter some notes");
            }
        }        
    }
    
    /**
     * Inner listening class for the prescribe button of the view. 
     * Gets the medicine to prescribe from the view, calls prescribe medicine
     * method from the appropriate service, and displays a message to 
     * the user to confirm it has been successful. Calls setMedicinesTable()
     * to update the view. 
     * @author Glenn McKnight
     *
     */
    class PrescribeButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {            
            try{
                if(view.getMedicineQuantity() != 0){
                                 
                	String medicineName = medService.getMedicine(view.getSelectedMedName().toString()).getMedName();
                    medService.prescribeMedicine(view.getMedicinesPatientID(), loggedInUserId, medicineName, view.getMedicineQuantity());
                    
                    view.displayMessage("Medicine prescribed");
                    setMedicinesTable();
                }else{
                    view.displayMessage("Please enter a medicine quantity");
                }
            }catch(NullPointerException ex){
                view.displayMessage("Select a medicine");
            }            
        }        
    }
    
    /**
     * Inner listening class for the create medicine button of the view. 
     * Gets the medicine information from the fields in the view, and calls
     * add medicine method from the appropriate service. Calls setMedicinesTable()
     * and clear fields method to update the view. Also displays message
     * to inform user of result. 
     * @author Glenn McKnight
     *
     */
    class CreateMedicineButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            try{ 
                medService.addMedicine(view.getMedName(), view.getMedDosage(), 
                        view.getCommonUses());

                setMedicinesTable();
                view.clearCreateMedicineFields();
                view.displayMessage("Medicine added");
            
            }catch(NullPointerException ex){
                view.displayMessage("Please enter all medicine information");
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
