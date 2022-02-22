package com.gmck.PatientManagementSystem.Messaging.Entities;

import java.time.LocalDateTime;

/**
 * Interface for user message entities.
 * To enforce the implementation of specified methods to ensure each message class
 * has consistent methods and format.
 * @author Glenn McKnight
 *
 */
public interface IMessage { 

	Long getId();

	String getUserId();
	void setUserId(String userId);

	String getSenderId();
	void setSenderId(String senderId);

	String getSenderName();
	void setSenderName(String senderName);

	LocalDateTime getSentAt();
	void setSentAt(LocalDateTime receivedAt);

	String getMessage();
	void setMessage(String message);	
	
	Boolean getIsRead();
	void setIsRead(Boolean isRead);
}