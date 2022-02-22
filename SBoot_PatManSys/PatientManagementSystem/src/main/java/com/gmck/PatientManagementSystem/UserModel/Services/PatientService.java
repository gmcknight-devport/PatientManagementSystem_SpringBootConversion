package com.gmck.PatientManagementSystem.UserModel.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.Appointment.Appointment;
import com.gmck.PatientManagementSystem.Appointment.Services.AppointmentService;
import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Address;
import com.gmck.PatientManagementSystem.UserModel.Entities.Patient;
import com.gmck.PatientManagementSystem.UserModel.Entities.PatientPrescription;
import com.gmck.PatientManagementSystem.UserModel.Repositories.PatientRepo;

/**
 * Patient Service implementation of IUserService interface. 
 * Performs operations on Patient Entities and interacts with the repository 
 * storing them. Includes private internal methods to assist interface functions. 
 * @author Glenn McKnight
 *
 */
@Service
public class PatientService implements IUserService {

	private PatientRepo repo;
	private IdService idService;
	private AppointmentService appointService;
		
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
	 */
	@Autowired
	public PatientService (PatientRepo repo, IdService idService, AppointmentService appointService) {
		this.repo = repo;
		this.idService = idService;
		this.appointService = appointService;
	}
	
	@Override
	public Patient getUser(String userId) {
		Patient patient = null;
		
		if(repo.existsById(userId)) {
			patient = repo.getById(userId);
			
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
		
		return patient;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Patient> getAllUsers() {
		List<Patient> patList = new ArrayList<>();
		patList = repo.findAll();
		return patList;
	}
	
	@Override
	public List<String> getUserInfo(){
		List<Patient> patList = repo.findAll();
		List<String> userInfo = new ArrayList<>();
		
		for(Patient p : patList) {
			String info = p.getUserId() + ", " + p.getTitle() + " " + p.getForename()
							+ " " + p.getSurname();
			userInfo.add(info);
		}
		
		return userInfo;
	}
	
	@Override
	public void deleteUser(String userId) {		
		List<Appointment> patAppointments = appointService.getPatientAppointments(userId);
		
		if(repo.existsById(userId)) {
			//Delete appointments first due to table relationships
			for(Appointment a : patAppointments) {
				appointService.deleteAppointment(a.getId());
			}		
						
			//Delete User		
			repo.deleteById(userId);			
			
		}else {
			ErrorUpdate.getInstance().updateObserver("User doesn't exist");
		}
	}
		
	@Override
	public void changePassword(String userId, char[] newPassword) {
		Patient patient;
		
		if(repo.existsById(userId)) {
			patient = repo.getById(userId);
			patient.setUserPassword(newPassword);
			
			saveUser(patient);
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
	}
	
	@Override
	public void updateUser(String userId, String title, String forename, String surname) {
		Patient patient;
		
		if(repo.existsById(userId)) {
			patient = repo.getById(userId);
			
			if(title != null) {
				patient.setTitle(title);
			}
			if(forename != null) {
				patient.setForename(forename);
			}
			if(surname != null) {
				patient.setSurname(surname);
			}
			saveUser(patient);
			
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
	}
	
	@Override
	public UserType getType() {
		return UserType.P;
	}
	
	/**
	 * Creates a new patient to be added to the repository. 
	 * Gets the highest patient ID stored in the repo, uses the idService to 
	 * generate the next id to be used for the new user, creates a new address and a new patient,
	 * and calls save method. Uses try and catch to ensure no null values are returned in the case
	 * of no users existing in the repo. 
	 * @param password - password for new user. 
	 * @param title - title for new user. 
	 * @param forename - forename for new user. 
	 * @param surname - surname for new user. 
	 * @param age - age for new user. 
	 * @param addressLine1 - address line 1 for new user address. 
	 * @param addressLine2 - address line w for new user address. 
	 * @param city - city for new user address. 
	 * @param postcode - postcode for new user address. 
	 * @param gender - gender for new user.
	 */
	public void addUser(char[] password, String title, String forename, String surname, int age, String addressLine1, 
			String addressLine2, String city, String postcode, char gender) {
		
		String userId;
		String highestId;
		
		try {
			highestId = repo.findFirstByOrderByUserIdDesc().getUserId();
			userId = idService.generateId(UserType.P, highestId);	
			
			Address address = new Address(userId, addressLine1, addressLine2, city, postcode);
			Patient patient = new Patient(userId, password, title, forename, surname, age, address, gender, 
					new ArrayList<String>(), new ArrayList<PatientPrescription>());
					
			saveUser(patient);
			
		}catch(NullPointerException e) {
			userId = idService.generateId(UserType.S, "");
			
			Address address = new Address(userId, addressLine1, addressLine2, city, postcode);
			Patient patient = new Patient(userId, password, title, forename, surname, age, address, gender, 
					new ArrayList<String>(), new ArrayList<PatientPrescription>());
			
			saveUser(patient);
		}
	}
	
	/**
	 * Updates the age for a specified user. 
	 * Checks repo to ensure user exists, and sets the new age from
	 * the parameter. 
	 * @param userId - ID of user to be edited. 
	 * @param age - new age to be set. 
	 */
	public void updateAge(String userId, int age) {
		Patient patient;
		
		if(repo.existsById(userId)) {
			patient = repo.getById(userId);
			patient.setAge(age);
			
			saveUser(patient);
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
	}
	
	/**
	 * Updates the gender for a specified user. 
	 * Checks repo to ensure user exists, and sets the new gender from
	 * the parameter. 
	 * @param userId - ID of user to be edited. 
	 * @param gender - new gender to be set. 
	 */
	public void updateGender(String userId, char gender) {
		Patient patient;
		
		if(repo.existsById(userId)) {
			patient = repo.getById(userId);
			patient.setGender(gender);
			
			saveUser(patient);
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
	}
	
	/**
	 * Adds new notes for a specified user. 
	 * Checks repo to ensure user exists, and adds the new notesfrom
	 * the parameter. 
	 * @param userId - ID of user to be edited. 
	 * @param notes - notes to be added ot the user. 
	 */
	public void addPatientNotes(String userId, String notes) {
		Patient patient;

		if(repo.existsById(userId)) {
			patient = repo.getById(userId);
			patient.addNotes(notes);

			saveUser(patient);
		}else {
			ErrorUpdate.getInstance().updateObserver("Patient couldn't be found");
		}
	}
	
	/**
	 * Updates the address of the specified user.
	 * Checks user exists in repo, creates new address from parameters, sets address
	 * for patient. 
	 * @param userId - ID of user to update.
	 * @param addressLine1 - first line of address to update to.
	 * @param addressLine2 - second line of address to update to.
	 * @param city - city of address to update to.
	 * @param postcode - postcode of address to update to.
	 */
	public void updateAddress(String userId, String addressLine1, String addressLine2, String city, String postcode) {
		Patient patient;
		Address address = new Address(userId, addressLine1, addressLine2, city, postcode);
		
		if(repo.existsById(userId)) {
			patient = repo.getById(userId);
			patient.setAddress(address);
			
			saveUser(patient);
		}else {
			ErrorUpdate.getInstance().updateObserver("User wasn't found so address can't be updated");
		}
	}
	
	/**
	 * Adds a new prescription for a specified user. 
	 * Checks repo to ensure user exists, creates a new prescription from 
	 * the parameters, and adds the prescription to user.
	 * @param userId - ID of user to be edited. 
	 * @param prescription - to be given to user.
	 * @param doctorId - ID of doctor prescribing it.
	 * @param prescriptionDate - date it ws prescribed. 
	 */
	public void addPatientPrescription(String userId, String prescription, String doctorId, LocalDate prescriptionDate) {
		Patient patient;
		PatientPrescription patPrescription = new PatientPrescription(userId, prescription, doctorId, prescriptionDate);
		
		if(repo.existsById(userId)) {
			patient = repo.getById(userId);
			patient.addPrescription(patPrescription);
			
			saveUser(patient);
		}
	}
	
	/**
	 * Gets the most recent prescription given to a patient.
	 * Checks to ensure patient exists in repo, gets latest prescription if
	 * there is one, and returns. 
	 * @param userId - ID of user to get prescription for. 
	 * @return String - latest user prescription.
	 */
	public String getCurrentPresription(String userId) {
		Patient patient;
		
		if(repo.existsById(userId)) {
			patient = repo.getById(userId);
			
			int index = patient.getPrescriptions().size() - 1;
			return patient.getPrescriptions().get(index).getPrescription();
		}else {
			return "No current prescription";
		}
	}
		
	/**
	 * Saves Patient to repo if it conforms to required entity constraints. 
	 * @param patient - user to be saved.
	 */
	private void saveUser(Patient patient) {	
		try {			
			repo.save(patient);
			
		}catch(ConstraintViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Patient does not conform to validation rules - ensure there aren't "
					+ "null or incorrectly formatted values");
		}catch(DataIntegrityViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Referenced foreign key field doesn't exist");
		}
	}
}
