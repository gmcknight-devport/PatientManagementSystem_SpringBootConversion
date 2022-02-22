package com.gmck.PatientManagementSystem.UserModel.Services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Address;
import com.gmck.PatientManagementSystem.UserModel.Entities.Doctor;
import com.gmck.PatientManagementSystem.UserModel.Repositories.DoctorRepo;

/**
 * Doctor Service implementation of IUserService interface. 
 * Performs operations on Doctor entities and interacts with the repository 
 * storing them. Includes private internal methods to assist interface functions. 
 * @author Glenn McKnight
 *
 */
@Service
public class DoctorService implements IUserService {

	private DoctorRepo repo;
	private IdService idService;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
	 */
	@Autowired
	public DoctorService(DoctorRepo repo, IdService idService) {
		this.repo = repo;
		this.idService = idService;
	}
	
	@Override
	public Doctor getUser(String userId) {
		Doctor Doc = null;
		
		if(repo.existsById(userId)) {
			Doc = repo.getById(userId);
			
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
		
		return Doc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Doctor> getAllUsers() {
		List<Doctor> DocList = new ArrayList<>();
		DocList = repo.findAll();
		return DocList;
	}

	@Override
	public List<String> getUserInfo(){
		List<Doctor> docList = repo.findAll();
		List<String> userInfo = new ArrayList<>();
		
		for(Doctor d : docList) {
			String info = d.getUserId() + ", " + d.getTitle() + " " + d.getForename()
							+ " " + d.getSurname();
			userInfo.add(info);
		}
		
		return userInfo;
	}
	
	@Override
	public void deleteUser(String userId) {
		if(repo.existsById(userId)) {
			repo.deleteById(userId);
		}else {
			ErrorUpdate.getInstance().updateObserver("User doesn't exist");
		}
	}

	@Override
	public void updateUser(String userId, String title, String forename, String surname) {
		Doctor Doc;
		
		if(repo.existsById(userId)) {
			Doc = repo.getById(userId);
			
			if(title != null) {
				Doc.setTitle(title);
			}
			if(forename != null) {
				Doc.setForename(forename);
			}
			if(surname != null) {
				Doc.setSurname(surname);
			}
			saveUser(Doc);
			
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
		
	}

	@Override
	public void changePassword(String userId, char[] newPassword) {
		Doctor Doc;
		
		if(repo.existsById(userId)) {
			Doc = repo.getById(userId);
			Doc.setUserPassword(newPassword);
			
			saveUser(Doc);
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}

	}
	
	@Override
	public UserType getType() {
		return UserType.D;
	}

	/**
	 * Creates a new doctor to be added to the repository. 
	 * Gets the highest doctor ID stored in the repo, uses the idService to 
	 * generate the next id to be used for the new user, creates a new address and a new doctor,
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
	 */
	public void addUser(char[] password, String title, String forename, String surname, String addressLine1, 
			String addressLine2, String city, String postcode) {		
		
		String highestId;
		String userId;		
		
		try {
			highestId = repo.findFirstByOrderByUserIdDesc().getUserId();
			userId = idService.generateId(UserType.D, highestId);
			
			Address address = new Address(userId, addressLine1, addressLine2, city, postcode);
			Doctor doc = new Doctor(userId, password, title, forename, surname, address, new ArrayList<>());	
			
			saveUser(doc);
			
		}catch(NullPointerException e) {
			userId = idService.generateId(UserType.S, "");
			
			Address address = new Address(userId, addressLine1, addressLine2, city, postcode);
			Doctor doc = new Doctor(userId, password, title, forename, surname, address, new ArrayList<>());
			
			saveUser(doc);
		}		
	}
	
	/**
	 * Updates the address of the specified user.
	 * Checks user exists in repo, creates new address from parameters, sets address
	 * for doctor. 
	 * @param userId - ID of user to update.
	 * @param addressLine1 - first line of address to update to.
	 * @param addressLine2 - second line of address to update to.
	 * @param city - city of address to update to.
	 * @param postcode - postcode of address to update to.
	 */
	public void updateAddress(String userId, String addressLine1, String addressLine2, String city, String postcode) {
		Doctor doctor;
		Address address = new Address(userId, addressLine1, addressLine2, city, postcode);
		
		if(repo.existsById(userId)) {
			doctor = repo.getById(userId);
			doctor.setSurgeryAddress(address);
			
			saveUser(doctor);
		}else {
			ErrorUpdate.getInstance().updateObserver("User wasn't found so address can't be updated");
		}
	}
		
	/**
	 * Gets a list of ratings for a specific Doctor.
	 * @param docId - ID to get ratings for. 
	 * @return List - of Double rating value.
	 */
	public List<Double> getDoctorRatings(String docId){
		Doctor doctor = repo.getById(docId);
		return doctor.getRatings();
	}
	
	/**
	 * Gets list of Doctor IDs, names, and average rating as a String. 
	 * Gets all Doctors from repo, get average of their ratings, adds 
	 * their basic information and the rating to a String in a list. 
	 * @return List - of Strings of Doctor information with average rating. 
	 */
	public List<String> getDoctorsWithRatings() {
		double rating;
		List<String> doctorsWithRatings = new ArrayList<>();
		List<Doctor> docList = repo.findAll();
				
		for(Doctor d : docList) {
			//Round to 2 decimal places
			rating = Math.round(getAverageRating(d.getUserId()) * 100);
			rating /= 100;
			
			doctorsWithRatings.add(d.getUserId() + ", " + d.getForename() + ", " + d.getSurname() + ", " + rating);
		}
		
		return doctorsWithRatings;
	}
	
	/**
	 * Adds a new rating to a specific doctor.
	 * Checks doctor exists in repo, and adds rating before saving changes. 
	 * @param doctorId - ID to have rating added to.
	 * @param rating - to add.
	 */
	public void addRating(String doctorId, int rating) {
		Doctor doctor;
		
		if(repo.existsById(doctorId)) {
			doctor = repo.getById(doctorId);
			doctor.addRating(rating);
			
			saveUser(doctor);
		}
	}
	
	/**
	 * Calculates the average of ratings for a specific doctor. 
	 * Checks user exists, gets ratings and averages them before returning. 
	 * @param userId - ID of user to average ratings for. 
	 * @return double - average rating. 
	 */
	private double getAverageRating(String userId) {
		Doctor doctor;
		List<Double> ratings = new ArrayList<>();
		double average = 0;
		
		if(repo.existsById(userId)) {
			doctor = repo.getById(userId);
			ratings = doctor.getRatings();
			
			average = ratings.stream().mapToDouble(a -> a).average().orElse(0.0);
		}
		
		return average;
	}
	
	/**
	 * Saves Doctor to repo if it conforms to required entity constraints. 
	 * @param Doctor - user to be saved.
	 */
	private void saveUser(Doctor Doc) {
		try {
			repo.save(Doc);
			
		}catch(ConstraintViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Patient does not conform to validation rules - ensure there aren't "
					+ "null or incorrectly formatted values");
		}catch(DataIntegrityViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Referenced foreign key field doesn't exist");
		}
	}
}
