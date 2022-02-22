package com.gmck.PatientManagementSystem.Messaging.Services;

import java.time.LocalDateTime;
import java.util.List;

import com.gmck.PatientManagementSystem.Messaging.Entities.IMessage;
import com.gmck.PatientManagementSystem.UserModel.UserType;

/**
 * Interface for MessageService to enforce the implementation of specified methods to ensure proper 
 * working of a message service class - allows operations to be carried out on
 * data from repository. 
 * @author Glenn McKnight
 *
 */
public interface IMessageService {

	/**
	 * Creates a message and sends it to the specific user, saving it in the 
	 * appropriate repository. 
	 * @param userId - ID of user to be sent message.
	 * @param senderId - ID of user sending the message. 
	 * @param senderName - name of user sending the message. 
	 * @param receivedAt - time message was received/sent (either can be used
	 * 		in practice).
	 * @param messageText - the message to be sent. 
	 */
	void createMessage(String userId, String senderId, String senderName, LocalDateTime receivedAt, String messageText);
	
	/**
	 * Delete a specified message from the repository by the ID parameter.
	 * Must ensure message exists first to avoid exception.  
	 * @param messageId - the unique ID of a message. 
	 */
	void deleteMessage(Long messageId);
	
	/**
	 * Delete a specified message from the repository by multiple parameters
	 * if the id is not known.
	 * Searches repository via custom method to see if a message matches these parameters. 
	 * Must ensure message exists first to avoid exception.  
	 * @param userId - ID of the user message was sent to.
	 * @param senderId - ID of the user who sent the message.
	 * @param sentAt - when the message was sent.
	 */
	void deleteMessage(String userId, String senderId, LocalDateTime sentAt);
	
	/**
	 * Generic getter to return a list of messages for a user ID that extend the IMessage interface - 
	 * specific to the implementing class. 
	 * @param <T> - the defined concrete implementing class of IMessage. 
	 * @param userId - the ID of the user to get messages for. 
	 * @return List - of messages for a user ID. 
	 */
	<T extends IMessage> List<?> getUserMessages(String userId);
	
	/**
	 * Sets a message to read. Check message exists in repo, set approved to true, and
	 * call method to save message. 
	 * @param userId - ID of user who received the message.
	 * @param senderId - ID of the user who sent the message. 
	 * @param sentAt - when the message was sent.
	 */
	void messageRead(String userId, String senderId, LocalDateTime sentAt);
	
	/**
	 * Save a message after operations have been carried out. 
	 */
	void saveMessage();
	
	/**
	 * Return the user type for this message. 
	 * @return UserType - for specific message class. 
	 */
	UserType getType();
}