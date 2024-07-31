package com.gmck.PatientManagementSystem.UserModel.Services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Secretary;
import com.gmck.PatientManagementSystem.UserModel.Repositories.SecretaryRepo;

/**
 * Secretary Service implementation of IUserService interface. 
 * Performs operations on Secretary entities and interacts with the repository 
 * storing them. Includes private internal methods to assist interface functions. 
 * @author Glenn McKnight
 *
 */
@Service
public class SecretaryService implements IUserService {

	private SecretaryRepo repo;
	private IdService idService;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
	 */
	@Autowired
	public SecretaryService(SecretaryRepo repo, IdService idService) {
		this.repo = repo;
		this.idService = idService;
	}
	
	@Override
	public Secretary getUser(String userId) {
		Secretary sec;
				
		if(repo.existsById(userId)) {
			sec = repo.getById(userId);
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
			sec = null;
		}
		
		return sec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Secretary> getAllUsers() {
		List<Secretary> secList = new ArrayList<>();
		secList = repo.findAll();
		return secList;
	}

	@Override
	public List<String> getUserInfo(){
		List<Secretary> secList = repo.findAll();
		List<String> userInfo = new ArrayList<>();
		
		for(Secretary s : secList) {
			String info = s.getUserId() + ", " + s.getTitle() + " " + s.getForename()
							+ " " + s.getSurname();
			userInfo.add(info);
		}
		
		return userInfo;
	}
	
	@Override
	public void deleteUser(String userId) {
		if(repo.existsById(userId) && repo.findAll().size() > 1) {
			repo.deleteById(userId);
		}else {
			ErrorUpdate.getInstance().updateObserver("There must be one secretary user at all times");
		}
	}

	@Override
	public void updateUser(String userId, String title, String forename, String surname) {
		Secretary sec;
		
		if(repo.existsById(userId)) {
			sec = repo.getById(userId);
			
			if(title != null) {
				sec.setTitle(title);
			}
			if(forename != null) {
				sec.setForename(forename);
			}
			if(surname != null) {
				sec.setSurname(surname);
			}
			saveUser(sec);
			
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
		
	}

	@Override
	public void changePassword(String userId, char[] newPassword) {
		Secretary sec;
		
		if(repo.existsById(userId)) {
			sec = repo.getById(userId);
			sec.setUserPassword(newPassword);
			
			saveUser(sec);
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}

	}
	
	@Override
	public UserType getType() {
		return UserType.S;
	}

	/**
	 * Creates a new Secretary to be added to the repository. 
	 * Gets the highest secretary ID stored in the repo, uses the idService to 
	 * generate the next id to be used for the new user, creates a new secretary,
	 * and calls save method. Uses try and catch to ensure no null values are returned in the case
	 * of no users existing in the repo. 
	 * @param password - password for new user. 
	 * @param title - title for new user. 
	 * @param forename - forename for new user. 
	 * @param surname - surname for new user. 
	 */
	public void addUser(char[] password, String title, String forename, String surname) {
		String highestId;
		String userId;	
		
		try {
			highestId = repo.findFirstByOrderByUserIdDesc().getUserId();
			userId = idService.generateId(UserType.S, highestId);
			
			Secretary sec = new Secretary(userId, password, title, forename, surname);			
			saveUser(sec);
			
		}catch(NullPointerException e) {
			userId = idService.generateId(UserType.S, "");
			
			Secretary sec = new Secretary(userId, password, title, forename, surname);			
			saveUser(sec);
		}
	}
	
	/**
	 * Saves Secretary to repo if it conforms to required entity constraints. 
	 * @param Secretary - user to be saved.
	 */
	private void saveUser(Secretary sec) {
		try {
			repo.save(sec);
			
		}catch(ConstraintViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Patient does not conform to validation rules - ensure there aren't "
					+ "null or incorrectly formatted values");
		}catch(DataIntegrityViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Referenced foreign key field doesn't exist");
		}
	}
}
