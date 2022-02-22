package com.gmck.PatientManagementSystem.UserModel.Services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Administrator;
import com.gmck.PatientManagementSystem.UserModel.Repositories.AdministratorRepo;

/**
 * Administrator Service implementation of IUserService interface. 
 * Performs operations on Administrator entities and interacts with the repository 
 * storing them. Includes private internal methods to assist interface functions. 
 * @author Glenn McKnight
 *
 */
@Service
public class AdministratorService implements IUserService {

	private AdministratorRepo repo;
	private IdService idService;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
	 */
	@Autowired
	public AdministratorService(AdministratorRepo repo, IdService idService) {
		this.repo = repo;
		this.idService = idService;
	}
	
	@Override
	public Administrator getUser(String userId) {
		Administrator admin = null;
		
		if(repo.existsById(userId)) {
			admin = repo.getById(userId);
			
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
		
		return admin;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Administrator> getAllUsers() {
		List<Administrator> adminList = new ArrayList<>();
		adminList = repo.findAll();
		return adminList;
	}

	@Override
	public List<String> getUserInfo(){
		List<Administrator> adminList = repo.findAll();
		List<String> userInfo = new ArrayList<>();
		
		for(Administrator a : adminList) {
			String info = a.getUserId() + ", " + a.getTitle() + " " + a.getForename()
							+ " " + a.getSurname();
			userInfo.add(info);
		}
		
		return userInfo;
	}
	
	@Override
	public void deleteUser(String userId) {
		if(repo.existsById(userId) && repo.findAll().size() > 1) {
			repo.deleteById(userId);
		}else {
			ErrorUpdate.getInstance().updateObserver("There must be one admin user at all times");
		}
	}

	@Override
	public void updateUser(String userId, String title, String forename, String surname) {
		Administrator admin;
		
		if(repo.existsById(userId)) {
			admin = repo.getById(userId);
			
			if(title != null) {
				admin.setTitle(title);
			}
			if(forename != null) {
				admin.setForename(forename);
			}
			if(surname != null) {
				admin.setSurname(surname);
			}
			saveUser(admin);
			
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}
		
	}

	@Override
	public void changePassword(String userId, char[] newPassword) {
		Administrator admin;
		
		if(repo.existsById(userId)) {
			admin = repo.getById(userId);
			admin.setUserPassword(newPassword);
			
			saveUser(admin);
		}else {
			ErrorUpdate.getInstance().updateObserver("User not found");
		}

	}

	@Override
	public UserType getType() {
		return UserType.A;
	}
	
	/**
	 * Creates a new Administrator to be added to the repo. 
	 * Gets highest current id value and passes it to id service to generate
	 * a new unique ID (note: system must have 1 administrator at all times so there is
	 * no risk of null exception). Creates new Administrator and calls save method. 
	 * @param password - password for new user. 
	 * @param title - title for new user. 
	 * @param forename - forename for new user. 
	 * @param surname - surname for new user. 
	 */
	public void addUser(char[] password, String title, String forename, String surname) {
		String highestId = repo.findFirstByOrderByUserIdDesc().getUserId();
		String userId = idService.generateId(UserType.A, highestId);		
				
		Administrator admin = new Administrator(userId, password, title, forename, surname);
		
		saveUser(admin);

	}
	
	/**
	 * Saves Administrator to repo if it conforms to required entity constraints. 
	 * @param Administrator - user to be saved.
	 */
	private void saveUser(Administrator admin) {
		try {
			repo.save(admin);
			
		}catch(ConstraintViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Patient does not conform to validation rules - ensure there aren't "
					+ "null or incorrectly formatted values");
		}catch(DataIntegrityViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Referenced foreign key field doesn't exist");
		}
	}
}
