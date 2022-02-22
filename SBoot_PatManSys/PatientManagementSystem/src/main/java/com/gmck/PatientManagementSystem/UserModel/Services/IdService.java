package com.gmck.PatientManagementSystem.UserModel.Services;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;

/**
 * Service to generate new User IDs based on highest value passed as a parameter along with
 * the UserType parameter.
 * @author Glenn McKnight
 *
 */
@Service
public class IdService {
	
	/**
	 * Generate a new user ID.
	 * Checks the highestId param is not empty or null, if it is: returns UserType and 1001
	 * to be first of this user. 
	 * If the highestId is 5 it parses to the last 4 digits to remove 
	 * any potential UserType prefixes.
	 * Will thrown exceptions and report errors to user. 
	 * Creates a new id by adding 1 to the parsed value.  
	 * @param type - UserType to generate ID for. 
	 * @param highestId - current highest ID for that UserType. 
	 * @return String - new unique userId.
	 */
	public String generateId(@NotNull UserType type, String highestId) {
		String newId = null;
		int idNumber;

		if(highestId.isEmpty() || highestId == null) {
			newId = type.toString() + "1001";
			
		}else if(highestId.length() == 5){
			try {
				idNumber = Integer.parseInt(highestId.substring(1, 5));
				idNumber++;
				
				newId = type.toString() + Integer.toString(idNumber);
				
			}catch(NumberFormatException e) {
				ErrorUpdate.getInstance().updateObserver("Couldn't generate - incorrect ID format");
				throw new NumberFormatException();
			}
		}else {
			ErrorUpdate.getInstance().updateObserver("Couldn't generate new userId");
			throw new IllegalArgumentException();
		}
		 
		return newId;
	}
}
