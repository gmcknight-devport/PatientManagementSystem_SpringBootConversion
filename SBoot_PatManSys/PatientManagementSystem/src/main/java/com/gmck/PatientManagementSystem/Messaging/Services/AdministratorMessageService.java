package com.gmck.PatientManagementSystem.Messaging.Services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.Messaging.Entities.AdministratorMessage;
import com.gmck.PatientManagementSystem.Messaging.Repositories.AdministratorMessageRepo;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Administrator;
import com.gmck.PatientManagementSystem.UserModel.Services.AdministratorService;

/**
 * Administrator Message Service implementation of IMessageService interface. 
 * Performs operations on admin message entities and interacts with the repository 
 * storing them. 
 * @author Glenn McKnight
 *
 */
@Service
public class AdministratorMessageService implements IMessageService, ICreateMessageToAll {
	
	private AdministratorMessage adminMessage;	
	private AdministratorMessageRepo adminMessRepo;
	private AdministratorService adminService;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables.
	 */
	@Autowired
	public AdministratorMessageService(AdministratorMessageRepo adminMessRepo, AdministratorService adminService) {
		this.adminMessRepo = adminMessRepo;
		this.adminService = adminService;
	}
	
	@Override
	public void createMessage(String userId, String senderId, String senderName, LocalDateTime sentAt, String messageText) {
		adminMessage = new AdministratorMessage();
		
		adminMessage.setUserId(userId);
		adminMessage.setSenderId(senderId);
		adminMessage.setSenderName(senderName);
		adminMessage.setSentAt(sentAt.truncatedTo(ChronoUnit.SECONDS));
		adminMessage.setMessage(messageText);
		adminMessage.setIsRead(false);
		
		saveMessage();
	}
		
	@Override
	public void deleteMessage(Long messageId) {
		if(adminMessRepo.existsById(messageId)) {
			adminMessRepo.deleteById(messageId);
		}else {
			ErrorUpdate.getInstance().updateObserver("Couldn't delete message");
		}
	}
	
	@Override
	public void deleteMessage(String userId, String senderId, LocalDateTime sentAt) {
		adminMessage = adminMessRepo.getSpecificMessage(userId, senderId, sentAt.truncatedTo(ChronoUnit.SECONDS));
		adminMessRepo.delete(adminMessage);
	}
	
	@Override
	public List<AdministratorMessage> getUserMessages(String userId) {		
		return adminMessRepo.getMessagesById(userId);
	}
	
	@Override
	public void messageRead(String userId, String senderId, LocalDateTime sentAt) {
		adminMessage = adminMessRepo.getSpecificMessage(userId, senderId, sentAt.truncatedTo(ChronoUnit.SECONDS));		
		adminMessage.setIsRead(true);
		
		saveMessage();		
	}
	
	@Override
	public void saveMessage() {		
		try {
			adminMessRepo.save(adminMessage);
			
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
	public final UserType getType() {
		return UserType.A;
	}	
	
	@Override
	public void createMessageToAll(String senderId, String senderName, LocalDateTime sentAt, String messageText) {
		for(Administrator a : adminService.getAllUsers()) {
			createMessage(a.getUserId(), senderId, senderName, sentAt, messageText);
		}		
	}
}
