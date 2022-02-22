package com.gmck.PatientManagementSystem.LoginServices;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.User;
import com.gmck.PatientManagementSystem.UserModel.Repositories.*;
import com.gmck.PatientManagementSystem.util.EnumLookup;

/**
 * Service class for login operations.
 * Performs authentication for user logins. 
 * @author Glenn McKnight
 *
 */
@Service
public class LoginService {

	private AdministratorRepo adminRepo;
	private DoctorRepo docRepo;
	private PatientRepo patRepo;
	private SecretaryRepo secRepo;
	
	private Map<UserType, JpaRepository<? extends User, String>> repoCache = new HashMap<>();
		
	/**
     * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
     */
	@Autowired
	public LoginService(AdministratorRepo adminRepo, DoctorRepo docRepo, PatientRepo patRepo, SecretaryRepo secRepo) {
		this.adminRepo = adminRepo;
		this.docRepo = docRepo;
		this.patRepo = patRepo;
		this.secRepo = secRepo;
	}
	
	/**
	 * PostCnstruct initialisation method to populate a map with each
	 * UserType and the corresponding repository.
	 */
	@PostConstruct
    private void initMyServiceCache() {               	
    	repoCache.put(UserType.A, adminRepo);        	
    	repoCache.put(UserType.D, docRepo);
    	repoCache.put(UserType.P, patRepo);
    	repoCache.put(UserType.S, secRepo);
    }
	
	/**
	 * Authenticate the parameters against the stored user name and password information. 
	 * Get repo type from the cache, check user exits using userId, get the user
	 * by ID from the repo, and authenticate the user. If authentication fails, 
	 * report the appropriate error message. 
	 * @param userId - The userId to validate.
	 * @param userPassword - The user password to authenticate. 
	 * @return boolean - true if authentication was successful.
	 */
	public Boolean authenticateUser(String userId, char[] userPassword){
		UserType type;
		User user;
		
		try {			
			//Get repo type
	    	type = EnumLookup.lookup(UserType.class, userId.substring(0, 1));    	
	    	JpaRepository<? extends User, String> repo = repoCache.get(type);
	    	
	    	//Check user exists
	    	if(repo.existsById(userId)) {
	    		user = repo.getById(userId);
	    		
	    		//Authenticate user and return true of false
	    		if(user.authenticate(userId, userPassword)) {
	    			return true;
	    		}else {
	    			ErrorUpdate.getInstance().updateObserver("Incorrect password");
	    			return false;
	    		}    		
	    	}else {
	    		ErrorUpdate.getInstance().updateObserver("Username doesn't exist");
	    		return false;
	    	}
	    	
		}catch (RuntimeException ex) {
			ErrorUpdate.getInstance().updateObserver("User type not found");
    		return false;	
		}
	}	
}
