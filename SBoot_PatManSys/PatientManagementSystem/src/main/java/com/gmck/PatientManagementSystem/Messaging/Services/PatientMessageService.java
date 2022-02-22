package com.gmck.PatientManagementSystem.Messaging.Services;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.Messaging.Entities.PatientMessage;
import com.gmck.PatientManagementSystem.Messaging.Repositories.PatientMessageRepo;
import com.gmck.PatientManagementSystem.UserModel.UserType;

/**
 * Patient Message Service implementation of IMessageService interface. 
 * Performs operations on patient message entities and interacts with the repository 
 * storing them. 
 * @author Glenn McKnight
 *
 */
@Service
public class PatientMessageService implements IMessageService {
		
	private PatientMessage patMessage;	
	private PatientMessageRepo patMessRepo;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables.
	 */
	@Autowired
	public PatientMessageService(PatientMessageRepo patMessRepo) {
		this.patMessRepo = patMessRepo;
	}
	
	@Override
	public void createMessage(String userId, String senderId, String senderName, LocalDateTime receivedAt, String messageText) {
		patMessage = new PatientMessage();
		
		patMessage.setUserId(userId);
		patMessage.setSenderId(senderId);
		patMessage.setSenderName(senderName);
		patMessage.setSentAt(receivedAt);
		patMessage.setMessage(messageText);
		patMessage.setIsRead(false);
		
		saveMessage();
	}
		
	@Override
	public void deleteMessage(Long messageId) {
		if(patMessRepo.existsById(messageId)) {
			patMessRepo.deleteById(messageId);
		}else {
			ErrorUpdate.getInstance().updateObserver("Couldn't delete message");
		}
	}
	
	@Override
	public void deleteMessage(String userId, String senderId, LocalDateTime sentAt) {
		try {
			patMessage = patMessRepo.getSpecificMessage(userId, senderId, sentAt);
			patMessRepo.delete(patMessage);
		}catch(NullPointerException e) {
			ErrorUpdate.getInstance().updateObserver("Couldn't delete message");
		}
	}
	
	@Override
	public List<PatientMessage> getUserMessages(String userId) {
		return patMessRepo.getMessagesById(userId);
	}
	
	@Override
	public void messageRead(String userId, String senderId, LocalDateTime sentAt) {
		patMessage = patMessRepo.getSpecificMessage(userId, senderId, sentAt);
		patMessage.setIsRead(true);
		
		saveMessage();
	}
	
	@Override
	public void saveMessage() {
		try {
			patMessRepo.save(patMessage);
			
		}catch(ConstraintViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Message does not conform to validation rules - ensure there aren't "
					+ "null or incorrectly formatted values");
		}catch(DataIntegrityViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Referenced foreign key field doesn't exist");
		}catch(IllegalArgumentException e) {
			ErrorUpdate.getInstance().updateObserver("Message contains null values");
		}		
	}

	@Override
	public UserType getType() {
		return UserType.P;
	}
}
