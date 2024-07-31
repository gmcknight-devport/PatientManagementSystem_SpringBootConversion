package com.gmck.PatientManagementSystem.Controllers;

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

import com.formdev.flatlaf.FlatDarculaLaf;
import com.gmck.PatientManagementSystem.Appointment.Appointment;
import com.gmck.PatientManagementSystem.Appointment.Services.AppointmentService;
import com.gmck.PatientManagementSystem.ErrorUpdate.IErrorUpdateObserver;
import com.gmck.PatientManagementSystem.LoginServices.ILogout;
import com.gmck.PatientManagementSystem.LoginServices.StrategySelectFactory;
import com.gmck.PatientManagementSystem.Medicine.Medicine;
import com.gmck.PatientManagementSystem.Medicine.Services.MedicineService;
import com.gmck.PatientManagementSystem.Messaging.Entities.SecretaryMessage;
import com.gmck.PatientManagementSystem.Messaging.Services.IDisplayMessage;
import com.gmck.PatientManagementSystem.Messaging.Services.PatientMessageService;
import com.gmck.PatientManagementSystem.Messaging.Services.SecretaryMessageService;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Secretary;
import com.gmck.PatientManagementSystem.UserModel.Entities.TempUser;
import com.gmck.PatientManagementSystem.UserModel.Services.DoctorService;
import com.gmck.PatientManagementSystem.UserModel.Services.PatientService;
import com.gmck.PatientManagementSystem.UserModel.Services.SecretaryService;
import com.gmck.PatientManagementSystem.UserModel.Services.TempUserService;
import com.gmck.PatientManagementSystem.Views.SecretaryView;

/**
 * Controller for the SecView and related services. 
 * Creates and sets up the SecView and responds to user input to carry 
 * out the required operations. Has inner classes to listen for actions. 
 * @author Glenn McKnight
 *
 */
@Controller
public class SecretaryController implements IController, IErrorUpdateObserver, ILogout, IDisplayMessage{

	@Pattern(regexp = "^[ADPST][0-9][0-9][0-9][0-9]$")
	private String loggedInUserId;    
    private SecretaryView view;
    private MedicineService medService;
    private AppointmentService appointService;
    private SecretaryService secService;
    private PatientService patService;
    private DoctorService docService;
    private SecretaryMessageService secMessageService; 
    private PatientMessageService patMessageService;
    private TempUserService tempUserService;
    private StrategySelectFactory strategy;
      
    /**
     * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
     */
    @Autowired
    public SecretaryController(MedicineService medService, AppointmentService appointService, SecretaryService secService,
    		PatientService patService, DoctorService docService, SecretaryMessageService secMessageService, 
    		PatientMessageService patMessageService, TempUserService tempUserService, StrategySelectFactory strategy) {
    	
    	this.medService = medService;
    	this.appointService = appointService;
    	this.secService = secService;
    	this.patService = patService;
    	this.docService = docService;
    	this.secMessageService = secMessageService;
    	this.patMessageService = patMessageService;
    	this.tempUserService = tempUserService;
    	this.strategy = strategy;    	
    }
    
    @Override
    public void setView() {
    	try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		}catch(Exception e){
			e.printStackTrace();
		}
    	
        view = new SecretaryView();
        view.setVisible(true);
        
        setLogoutHandler();
        setCreationRequests();
        setSecInfo();
        setUserMessages();
        setDeleteCombo();
        setAppointmentDoctorIDs();
        setAppointmentPatientIDs();
        setAppointmentApprovalRequests();
        setDeleteAppointmentTable();
        setAppointmentDateFormat();        
        setAppointmentTimes();
        setMedicinesTable();
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
        view.addApproveAccountButtonHandler(new ApproveAccountListener());
        view.addDeclineAccountButtonHandler(new DeclineAccountListener());
        view.addRemoveAccountButtonHandler(new DeletePatientListener());
        view.addApproveAppointmentButtonHandler(new ApproveAppointmentListener());
        view.addDeclineAppointmentButtonHandler(new DeclineAppointmentListener());
        view.addCreateAppointmentButtonHandler(new CreateAppointmentListener());
        view.addDeleteAppointmentButtonHandler(new DeleteAppointmentButtonListener());
        view.addPrescribeMedicineButtonHandler(new GiveMedicineButtonListener());
        view.addOrderMedicineButtonHandler(new OrderMedicineButtonListener());
        view.addDeleteMedicineButtonHandler(new DeleteMedicineButtonListener());
        view.addAppointmentPatIdChangeHandler(new SelectPatientAppointmentsListener());
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
        	List<SecretaryMessage> messList = secMessageService.getUserMessages(loggedInUserId);
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
     * Set user creation requests in the view. 
     * Gets all temp users from the service and adds those to a 
     * variable to be passed in the required format for the GUI. 
     */    
    private void setCreationRequests(){
        try{
        	int userNum = tempUserService.getAllUsers().size();
            String[] creation = new String[userNum];
            
            for(int i = 0; i < userNum; i++){
                
                creation[i] = tempUserService.getAllUsers().get(i).getUserId() + ", "
                		+ tempUserService.getAllUsers().get(i).getTitle() + ", "
                        + tempUserService.getAllUsers().get(i).getForename() + ", "
                        + tempUserService.getAllUsers().get(i).getSurname() + ", "
                        + tempUserService.getAllUsers().get(i).getAge();  
            }            
            view.setCreationJList(creation);  
            
        }catch(NullPointerException ex){
            System.out.println("No creation requests");
            ex.printStackTrace();
        }
    }
      
    /**
     * Sets delete combo box with all remaining patients.
     */
    private void setDeleteCombo(){
        try{
            List<String> tempPat = patService.getUserInfo();            
            view.setRemoveJCombo(tempPat);
            
        }catch(IllegalArgumentException ex){
            view.displayMessage("Failed to load patient data");
        }
    }
    
    /**
     * Sets UserInfo in the view - basic user information added to String to be passed.
     */
    private void setSecInfo(){
    	String secInfo = secService.getUser(loggedInUserId).getUserId() + ", " 
    			+ secService.getUser(loggedInUserId).getTitle() + " "
    			+ secService.getUser(loggedInUserId).getForename() + " "
    			+ secService.getUser(loggedInUserId).getSurname();
        
    	view.setSecInfoText(secInfo);
    }
          
    /**
     * Sets appointment approval requests in the view. 
     * Gets all requested appointments from the service and adds those to a 
     * variable to be passed in the required format for the GUI. 
     */  
    private void setAppointmentApprovalRequests(){  
        try{
        	List<Appointment> appointList = appointService.getRequestedAppointments();
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
                   
            view.setAppointmentApprovalRequests(colNames, appointArr); 
            
        }catch(NullPointerException ex){
            System.out.println("No unapproved appointments");
        }
    }
    
    /**
     * Sets table of appointments that can be deleted in the view.
     * Gets all patient appointments from the service bu user ID and adds those to a 
     * variable to be passed in the required format for the GUI. 
     */  
    private void setDeleteAppointmentTable() {
    	try{
        	List<Appointment> appointList = appointService.getPatientAppointments(view.getAppointDeletePatId());
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
                   
            view.setDeleteAppointments(colNames, appointArr); 
            
        }catch(NullPointerException ex){
            System.out.println("No unapproved appointments");
        }
    }
    
    /**
     * Sets the appointment date spinner in the view.
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
     * Sets JCombo in the view with info for all doctors. 
     */
    private void setAppointmentDoctorIDs(){        
        view.setDoctorCombo(docService.getUserInfo());  
    }
    
    /**
     * Sets JCombo in the view with info for all patients. 
     */
    private void setAppointmentPatientIDs(){
        view.setPatientCombos(patService.getUserInfo());
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
    //ErrorReporting observer update methods
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

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            	secMessageService.deleteMessage(Long.valueOf(view.getMessageToDelete()));
                setUserMessages();
            
            }catch(NullPointerException ex){
                view.displayMessage("Please select a message to delete");
            }catch(NumberFormatException ex) {
            	view.displayMessage("Couldn't find to delete - incorrect message ID");
            }
        }		    
    }    
    
    /**
     * Inner class that acts as a listener for approve account button. 
     * Gets the user ID from the view for the temp user, gets that user from the 
     * appropriate service, copies the information from this user to create a new
     * patient object, and deletes the previous temp user. 
     * Calls the setDeleteCombo(), setCreationRequests(), and setAppointmentPatientIDs()
     * to update the information displayed in the view. 
     * @author Glenn McKnight
     *
     */
    class ApproveAccountListener implements ActionListener{
    	TempUser tempUser;
    	String userId;
    	String defaultAddress = "Add address";
    	String defaultPostcode = "AA00 AAA";
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            try{        
            	userId = view.getCreationSelectedValue().substring(0, 5);
            	
            	tempUser = tempUserService.getUser(userId);
            	patService.addUser(tempUser.getPassword(), tempUser.getTitle(), tempUser.getForename(), tempUser.getSurname(), 
            			tempUser.getAge(), defaultAddress, defaultAddress, defaultAddress, defaultPostcode, tempUser.getGender());
            	
            	tempUserService.deleteUser(userId);
            	            	
                setDeleteCombo();
                setCreationRequests();
                setAppointmentPatientIDs();
                
            } catch(IllegalArgumentException ex){
                System.out.println(ex);
            } catch(NullPointerException ex){
                view.displayMessage("No user selected");
            }
        }        
    }
    
    /**
     * Inner class that acts as a listener for the decline account button of the view.
     * Gets the ID of the temp user to delete the account of from the view, 
     * and calls the deleteUser() method of the appropriate service. Calls
     * setCreationRequests() to update the view.  
     * @author Glenn McKnight
     *
     */
    class DeclineAccountListener implements ActionListener{
    	 @Override
         public void actionPerformed(ActionEvent e) {
             try{   
            	 tempUserService.deleteUser(view.getCreationSelectedValue().substring(0, 5));
                 setCreationRequests();
                              
             } catch(NullPointerException ex){
                 view.displayMessage("No user selected");
                 ex.printStackTrace();
             } 
         }            	
    }    
    
    /**
     * Inner listener class for delete patient button of the view. 
     * Gets the patient ID to delete from the view, and calls the deleteUser()
     * method in the appropriate service. Calls setDeleteCombo() and 
     * setAppointmentPatientIDs() to update the view. 
     * @author Glenn McKnight
     *
     */
    class DeletePatientListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{    
            	patService.deleteUser(view.getDeletePatientValue().substring(0, 5));
            	
                setDeleteCombo();
                setAppointmentPatientIDs();
                                
            }catch(NullPointerException ex){
                view.displayMessage("No user selected");
            }
        }        
    }
        
    /**
     * Inner listener class for approve appointment button of the view. 
     * Gets the id for the appointment from the view, and calls the approveAppointment()
     * method in the appropriate service. Calls setAppointmentApprovalRequests() to update
     * the view. 
     * @author Glenn McKnight
     *
     */
    class ApproveAppointmentListener implements ActionListener{
    	@Override
        public void actionPerformed(ActionEvent e) {
            try{  
            	appointService.approveAppointment(Long.valueOf(view.getAppointmentRequest()));
                setAppointmentApprovalRequests();
            
            } catch(NullPointerException ex){
                view.displayMessage("No user selected");
                
            } catch(NumberFormatException ex) {
            	view.displayMessage("Couldn't find appointment to approve - incorrect appointment ID");
            }
        }                
    }
    
    /**
     * Inner listener class for the decline appointment button of the view. 
     * Gets id of the appointment to be declined, calls declineAppoinment method
     * in the appropriate service, and gets information to create and send a message 
     * after declining appointment. Calls setAppointmentApprovalRequests() to 
     * update the view. 
     * @author Glenn McKnight
     *
     */
    class DeclineAppointmentListener implements ActionListener{
    	String senderName;
    	String message;
    	
        @Override
        public void actionPerformed(ActionEvent e) {
        	Secretary sec;
            try{      
            	sec = secService.getUser(loggedInUserId);
        
            	senderName = sec.getForename() + " " + 
            			sec.getSurname();
            	message = "Your appointment request has been declined as it is unavailable";
            	
            	appointService.declineAppointment(Long.valueOf(view.getAppointmentRequest()), 
            			loggedInUserId, senderName, message);
                setAppointmentApprovalRequests();
            
            }catch(NullPointerException ex){
                view.displayMessage("No user selected");
                ex.printStackTrace();
            } catch(NumberFormatException ex) {
            	view.displayMessage("Couldn't find appointment to decline - incorrect appointment ID");
            }
        }                
    }
       
    /**
     * Inner listening class to change the value of a text area on the view when 
     * a new item is selected from a drop-down menu.
     * @author Glenn McKnight
     *
     */
    class SelectPatientAppointmentsListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			setDeleteAppointmentTable();			
		}    	
    }
    
    /**
     * Inner listener class for delete appointment button in the view. 
     * Gets the id for the appointment to be deleted, and calls delete
     * appointment method from the appropriate service. Calls 
     * setDeleteAppointmentTable() to update the view. 
     * @author Glenn McKnight
     *
     */
    class DeleteAppointmentButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try{            	
            	appointService.deleteAppointment(Long.valueOf(view.getAppointmentToDelete()));  
                setDeleteAppointmentTable();
                
            }catch(NullPointerException ex){
                view.displayMessage("Please select an appointmnet to delete");
                ex.printStackTrace();
            }catch(NumberFormatException ex) {
            	view.displayMessage("Couldn't find appointment to decline - incorrect appointment ID");
            }
        }
    }
    
    /**
     * Inner listener class for create appointment button in the view. 
     * Gets all appointment information from the view, and calls create 
     * appointment method from the appropriate service. Calls setDeleteAppointmentTable()
     * to update the view. 
     * @author Glenn McKnight
     *
     */
    class CreateAppointmentListener implements ActionListener{
    	LocalTime startTime;
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            	startTime = view.getAppointmentTime();
            	appointService.createAppointment(view.getAppointmentPatient(), view.getAppointmentDoctor(), startTime, 
            			startTime.plusMinutes(30), view.getAppointmentDate(), true);
            
            setDeleteAppointmentTable();
            
            }catch(IllegalArgumentException ex){
                view.displayMessage("Couldn't create appointment");
            }
            catch(DateTimeParseException ex) {
            	view.displayMessage("Couldn't get appointment date/time");
            }
        }        
    }
    
    /**
     * Inner listener class for the order medicine button of the view. 
     * Checks medicine quantity is greater than 0, gets medicine name and 
     * quantity from the view, and orders the medicine. Calls setMedicineTable()
     * to update the view. 
     * @author Glenn McKnight
     *
     */
    class OrderMedicineButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
        	
        	try{
	            if(view.getMedicineQuantity() != 0){
	            	medService.orderMedicine(view.getSelectedMedName().toString(), view.getMedicineQuantity());
	                setMedicinesTable();
	                
	                view.displayMessage("Medicine ordered");
	            }else{
	                view.displayMessage("Please enter a medicine quantity");
	            }
        	}catch(NullPointerException ex){
                view.displayMessage("Select a medicine");
            }
        }
    }
    
    /**
     * Inner listener class for delete medicine button of the view. 
     * Gets the medicine name form the view, and calls remove medicine
     * from the appropriate service. Calls setMedicineTable() to update
     * the view. 
     * @author Glenn McKnight
     *
     */
    class DeleteMedicineButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            try{
            	medService.removeMedicine(view.getSelectedMedName().toString());
                setMedicinesTable();
                
                view.displayMessage("Medicine deleted");
                
            }catch(NullPointerException ex){
                view.displayMessage("Select a medicine");
            }
        }    
    }
    
    /**
     * Inner listener class for the give medicine button of the view. 
     * Gets the name of the medicine from the view, gets full medicine 
     * information from the ppropriate service, and prescribes the medicine
     * to the specified patient. Calls notifyPatient method of the inner
     * class to send a message informing them of the prescription, and setMedincinesable()
     * to update the view. 
     * @author Glenn McKnight
     *
     */
    class GiveMedicineButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Medicine medicine;
            
            try{
                if(view.getMedicineQuantity() != 0){
                                 
                    medicine = medService.getMedicine(view.getSelectedMedName().toString());
                    
                    String medicineName = medService.getMedicine(view.getSelectedMedName().toString()).getMedName();
                    medService.prescribeMedicine(view.getPrescriptionPatientID(), view.getPrescriptionDoctorID().substring(0, 5),  medicineName, view.getMedicineQuantity());
                    
                    notifyPatient(medicine);
                    setMedicinesTable();
                    
                    view.displayMessage("Medicine given to patient");
                }else{
                    view.displayMessage("Please enter a medicine quantity");
                }
            }catch(NullPointerException ex){
                view.displayMessage("Select a medicine");
            }
        }
        
        /**
         * Sends message to notify the patient of their new prescription. 
         * @param medicine - The medicine that's been subscribed. 
         */
        private void notifyPatient(Medicine medicine) {
        	String message;
        	String senderName = secService.getUser(loggedInUserId).getForename() + " " 
        			+ secService.getUser(loggedInUserId).getSurname();
        	
            message = "Your doctor has prescribed: "
                    + medicine.getMedName() 
                    +"." + "\n" + " I have sent this much of it to you: "
                    + view.getMedicineQuantity();
            
            patMessageService.createMessage(view.getPrescriptionPatientID(), loggedInUserId, senderName, 
            		LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), message);
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
        @Override
        public void actionPerformed(ActionEvent e) {
            setStrategy();
            disposeView();
        }        
    }        
}
