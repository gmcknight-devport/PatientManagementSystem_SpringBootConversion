package com.gmck.PatientManagementSystem.Messaging.Services;

import java.time.LocalDateTime;

/**
 * Interface to enforce implementation of methods required by any class messaging 
 * all users of a type. 
 * @author Glenn McKnight
 *
 */
public interface ICreateMessageToAll {

	/**
	 * Creates a message to all users of a selected type. Based on createMessage method
	 * in the IMessageService class. Requires a sender ID but only UserType is required
	 * for this parameter. 
	 * @param senderId - the ID to get the UserType from. 
	 * @param senderName - the name of the sender.
	 * @param sentAt - when the message was sent.
	 * @param messageText - the message information to send.
	 */
	public void createMessageToAll(String senderId, String senderName, LocalDateTime sentAt, String messageText);
}
