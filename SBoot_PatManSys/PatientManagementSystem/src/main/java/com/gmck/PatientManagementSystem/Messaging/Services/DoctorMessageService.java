package com.gmck.PatientManagementSystem.Messaging.Services;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.Messaging.Entities.DoctorMessage;
import com.gmck.PatientManagementSystem.Messaging.Repositories.DoctorMessageRepo;
import com.gmck.PatientManagementSystem.UserModel.UserType;

/**
 * Doctor Message Service implementation of IMessageService interface. 
 * Performs operations on doctor message entities and interacts with the repository 
 * storing them. 
 * @author Glenn McKnight
 *
 */
@Service
public class DoctorMessageService implements IMessageService {

	private DoctorMessage docMessage;	
	private DoctorMessageRepo docMessRepo;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables.
	 */
	@Autowired
	public DoctorMessageService(DoctorMessageRepo docMessRepo) {
		this.docMessRepo = docMessRepo;
	}
	
	@Override
	public void createMessage(String userId, String senderId, String senderName, LocalDateTime receivedAt, String messageText) {
		docMessage = new DoctorMessage();
		
		docMessage.setUserId(userId);
		docMessage.setSenderId(senderId);
		docMessage.setSenderName(senderName);
		docMessage.setSentAt(receivedAt);
		docMessage.setMessage(messageText);
		docMessage.setIsRead(false);
		
		saveMessage();
	}
		
	@Override
	public void deleteMessage(Long messageId) {
		if(docMessRepo.existsById(messageId)) {
			docMessRepo.deleteById(messageId);
		}else {
			ErrorUpdate.getInstance().updateObserver("Couldn't delete message");
		}
	}
	
	@Override
	public void deleteMessage(String userId, String senderId, LocalDateTime sentAt) {
		docMessage = docMessRepo.getSpecificMessage(userId, senderId, sentAt);
		docMessRepo.delete(docMessage);
	}
	
	@Override
	public List<DoctorMessage> getUserMessages(String userId) {		
		return docMessRepo.getMessagesById(userId);
	}
	
	@Override
	public void messageRead(String userId, String senderId, LocalDateTime sentAt) {
		docMessage = docMessRepo.getSpecificMessage(userId, senderId, sentAt);
		docMessage.setIsRead(true);
		
		saveMessage();
	}
	
	@Override
	public void saveMessage() {
		try {
			docMessRepo.save(docMessage);
			
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
		return UserType.D;
	}
}
