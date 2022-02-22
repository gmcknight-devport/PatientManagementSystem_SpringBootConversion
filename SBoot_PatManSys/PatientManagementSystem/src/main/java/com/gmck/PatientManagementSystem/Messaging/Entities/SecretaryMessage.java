package com.gmck.PatientManagementSystem.Messaging.Entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Entity class for SecretaryMessage. 
 * Concrete implementation of IMessage and corresponds to a database table. 
 * Contains all information about each secretary message with each variable having 
 * validation annotations.
 * @author Glenn McKnight
 *
 */
@Entity
public class SecretaryMessage implements IMessage {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Pattern(regexp = "^[S][0-9][0-9][0-9][0-9]$")
	@Column(name = "USER_ID")
	private String userId;
	
	@Pattern(regexp = "^[ADPST][0-9][0-9][0-9][0-9]$")
	@Column(name = "SENDER_ID")
	private String senderId;
	
	@NotBlank
	@Column(name = "SENDER_NAME")
	private String senderName;
	
	@NotNull
	@Column(name = "SENT_AT")
	private LocalDateTime sentAt;
	
	@NotBlank
	@Column(name = "MESSAGE")
	private String message;
	
	@NotNull
	@Column(name = "IS_READ")
	private Boolean isRead;
	
	public SecretaryMessage() {}

	public SecretaryMessage(Long id, String userId, String senderId, String senderName, LocalDateTime sentAt,
			String message) {
		this.id = id;
		this.userId = userId;
		this.senderId = senderId;
		this.senderName = senderName;
		this.sentAt = sentAt;
		this.message = message;
		this.isRead = false;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String getSenderId() {
		return senderId;
	}

	@Override
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	@Override
	public String getSenderName() {
		return senderName;
	}

	@Override
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	@Override
	public LocalDateTime getSentAt() {
		return sentAt;
	}

	@Override
	public void setSentAt(LocalDateTime receivedAt) {
		this.sentAt = receivedAt;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}	
	
	@Override
	public Boolean getIsRead() {
		return isRead;
	}

	@Override
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;		
	}

}
