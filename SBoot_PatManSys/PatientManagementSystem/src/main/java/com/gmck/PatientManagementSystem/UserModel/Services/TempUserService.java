package com.gmck.PatientManagementSystem.UserModel.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Address;
import com.gmck.PatientManagementSystem.UserModel.Entities.TempUser;
import com.gmck.PatientManagementSystem.UserModel.Repositories.TempUserRepo;

/**
 * TempUser Service implementation of IUserService interface. 
 * Performs operations on TempUser entities and interacts with the repository 
 * storing them. Includes private internal methods to assist interface functions. 
 * @author Glenn McKnight
 *
 */
@Service
public class TempUserService {

	private TempUserRepo repo;
	private IdService idService;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
	 */
	@Autowired
	public TempUserService(TempUserRepo repo, IdService idService) {
		this.repo = repo;
		this.idService = idService;
	}
	
	/**
	 * Get a user by the specified ID parameter.
	 * @param userId - ID of the user to return. 
	 * @return User - for specific user entity.
	 */
	public TempUser getUser(String userId) {
		TempUser user = null;
		
		if(repo.existsById(userId)) {
			user = repo.getById(userId);
		}else {
			ErrorUpdate.getInstance().updateObserver("Couldn't find temp user");
		}
		
		return user;
	}
	
	/**
	 * Getter to return all users of TempUser entity from repo.
	 * @return List - of TempUsers. 
	 */
	public List<TempUser> getAllUsers() {		
		List<TempUser> userList = new ArrayList<>();
		userList = repo.findAll();
		return userList;
	}

	/**
	 * Delete the user specified by the parameter.
	 * Check the user exists in the repo, remove the user from the repo, 
	 * and save result. 
	 * @param userId - ID of user to be deleted. 
	 */
	public void deleteUser(String userId) {
		if(repo.existsById(userId)) {
			repo.deleteById(userId);
		}else {
			ErrorUpdate.getInstance().updateObserver("User doesn't exist");
		}
	}
	
	/**
	 * Creates a new temp user to be added to the repository. 
	 * Gets the highest temp user ID stored in the repo, uses the idService to 
	 * generate the next id to be used for the new user, creates a new address and a new temp user,
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
	public void addUser(char[] password, String title, String forename, String surname, int age, String addressLine1, 
			String addressLine2, String city, String postcode) {
		String highestId;
		String userId;
				
		char gender = getGender(title);
		
		try {
			highestId = repo.findFirstByOrderByUserIdDesc().getUserId();
			userId = idService.generateId(UserType.P, highestId);	
			
			Address address = new Address(userId, addressLine1, addressLine2, city, postcode);
			TempUser temp = new TempUser(userId, password, title, forename, surname, age, address, gender);
					
			repo.save(temp);
			
		}catch(NullPointerException e) {
			userId = idService.generateId(UserType.S, "");
			
			Address address = new Address(userId, addressLine1, addressLine2, city, postcode);
			TempUser temp = new TempUser(userId, password, title, forename, surname, age, address, gender);
			
			repo.save(temp);
		}
	}
	
	/**
	 * Gets gender based on the title the has been input by the temp user
	 * account request. 
	 * @param title - title of temp user.
	 * @return char - that confrms to a gender based on title. 
	 */
	private char getGender(String title) {
		if(title == "Mr") {
			return 'M';
		}else if(title == "Mrs") {
			return 'F';
		}else {
			return 'O';
		}
	}
}
