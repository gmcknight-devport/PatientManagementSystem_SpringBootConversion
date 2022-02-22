package com.gmck.PatientManagementSystem.Messaging.Services;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.Messaging.Entities.SecretaryMessage;
import com.gmck.PatientManagementSystem.Messaging.Repositories.SecretaryMessageRepo;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Secretary;
import com.gmck.PatientManagementSystem.UserModel.Services.SecretaryService;

/**
 * Secretary Message Service implementation of IMessageService interface. 
 * Performs operations on secretary message entities and interacts with the repository 
 * storing them. 
 * @author Glenn McKnight
 *
 */
@Service
public class SecretaryMessageService implements IMessageService, ICreateMessageToAll {

	private SecretaryMessage secMessage;		
	private SecretaryMessageRepo secMessRepo;
	private SecretaryService secService;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables.
	 */
	@Autowired
	public SecretaryMessageService(SecretaryMessageRepo secMessRepo, SecretaryService secService) {
		this.secMessRepo = secMessRepo;
		this.secService = secService;
	}
	
	@Override
	public void createMessage(String userId, String senderId, String senderName, LocalDateTime receivedAt, String messageText) {
		secMessage = new SecretaryMessage();
		
		secMessage.setUserId(userId);
		secMessage.setSenderId(senderId);
		secMessage.setSenderName(senderName);
		secMessage.setSentAt(receivedAt);
		secMessage.setMessage(messageText);
		secMessage.setIsRead(false);
		
		saveMessage();
	}
	
	@Override
	public void deleteMessage(Long messageId) {
		if(secMessRepo.existsById(messageId)) {
			secMessRepo.deleteById(messageId);
		}else {
			ErrorUpdate.getInstance().updateObserver("Couldn't delete message");
		}
	}
		
	@Override
	public void deleteMessage(String userId, String senderId, LocalDateTime sentAt) {
		secMessage = secMessRepo.getSpecificMessage(userId, senderId, sentAt);
		secMessRepo.delete(secMessage);
	}
	
	@Override
	public List<SecretaryMessage> getUserMessages(String userId) {	
		return secMessRepo.getMessagesById(userId);
	}
	
	@Override
	public void messageRead(String userId, String senderId, LocalDateTime sentAt) {
		secMessage = secMessRepo.getSpecificMessage(userId, senderId, sentAt);
		secMessage.setIsRead(true);
		
		saveMessage();
	}
	
	@Override
	public void saveMessage() {
		try {
			secMessRepo.save(secMessage);
			
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
		return UserType.S;
	}
	
	@Override
	public void createMessageToAll(String senderId, String senderName, LocalDateTime sentAt, String messageText) {
		for(Secretary s : secService.getAllUsers()) {
			createMessage(s.getUserId(), senderId, senderName, sentAt, messageText);
		}		
	}
}
