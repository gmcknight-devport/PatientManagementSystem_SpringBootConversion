package com.gmck.PatientManagementSystem.Messaging.Services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.gmck.PatientManagementSystem.Messaging.Entities.AdministratorMessage;
import com.gmck.PatientManagementSystem.Messaging.Repositories.AdministratorMessageRepo;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Administrator;
import com.gmck.PatientManagementSystem.UserModel.Services.AdministratorService;

@ExtendWith(MockitoExtension.class)
class AdministratorMessageServiceTest {
		
	@Mock
	private AdministratorMessageRepo adminRepo;
	@Mock
	private AdministratorMessage adminMessage;
	@Mock
	private AdministratorService adminService;
	
	@InjectMocks
	@Autowired
	private AdministratorMessageService instance;
			
	@Test
	void createMessage() {		
		AdministratorMessage message = getAdminMessageData();		
		instance.createMessage(message.getUserId(), message.getSenderId(), message.getSenderName(), message.getSentAt(), message.getMessage());
		
		ArgumentCaptor<AdministratorMessage> argCaptor = ArgumentCaptor.forClass(AdministratorMessage.class);
		verify(adminRepo).save(argCaptor.capture());
		
		AdministratorMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
	
	@Test
	void deleteMessageById() {
		AdministratorMessage message = getAdminMessageData();	
		Long id = message.getId();
		
		Mockito.doReturn(true)
		.when(adminRepo)
		.existsById(id);
		
		instance.deleteMessage(id);
		
		verify(adminRepo, times(1)).deleteById(id);		
	}
	
	@Test
	void deleteMessage() {
		AdministratorMessage message = getAdminMessageData();		
		
		instance.deleteMessage(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		verify(adminRepo, times(1)).delete(any());		
	}
		
	@Test
	void getUserMessages() {
		AdministratorMessage[] obj = new AdministratorMessage[1];
		obj[0] = getAdminMessageData();
		List<AdministratorMessage> message = (List<AdministratorMessage>)Arrays.asList(obj);
		
		Mockito.doReturn(message)
		.when(adminRepo)
		.getMessagesById(message.get(0).getUserId());
		
		List<AdministratorMessage> allUserMessages = instance.getUserMessages(message.get(0).getUserId());
		AdministratorMessage expMessage = message.get(0);		
		
		assertThat(allUserMessages.get(0)).usingRecursiveComparison().isEqualTo(expMessage);
	}
		
	@Test
	void messageRead() {
		AdministratorMessage message = getAdminMessageData();		
		
		Mockito.doReturn(message)
		.when(adminRepo)
		.getSpecificMessage(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		instance.messageRead(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		ArgumentCaptor<AdministratorMessage> argCaptor = ArgumentCaptor.forClass(AdministratorMessage.class);
		verify(adminRepo).save(argCaptor.capture());
		
		AdministratorMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage.getIsRead()).isEqualTo(message.getIsRead());		
	}
	
	@Test
	void saveMessage() {
		AdministratorMessage message = getAdminMessageData();
		instance.createMessage(message.getUserId(), message.getSenderId(), message.getSenderName(), 
				message.getSentAt(), message.getMessage());
				
		ArgumentCaptor<AdministratorMessage> argCaptor = ArgumentCaptor.forClass(AdministratorMessage.class);
		verify(adminRepo).save(argCaptor.capture());
		
		AdministratorMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
	
	@Test
	void getType() {
		UserType expResult = UserType.A;
		UserType result = instance.getType();
		
		assertThat(result).isEqualTo(expResult);
	}
	
	@Test
	void createMessageToAll() {		
		List<Administrator> adminList = new ArrayList<>();
		Administrator admin = new Administrator("A1003", "password".toCharArray(), "Mrs.", "Missy", "Missington");
		adminList.add(admin);
		AdministratorMessage message = getAdminMessageData();		
		
		Mockito.doReturn(adminList)
		.when(adminService)
		.getAllUsers();	
		
		instance.createMessageToAll(message.getSenderId(), message.getSenderName(), message.getSentAt(), message.getMessage());
				
		ArgumentCaptor<AdministratorMessage> argCaptor = ArgumentCaptor.forClass(AdministratorMessage.class);
		verify(adminRepo).save(argCaptor.capture());
		
		AdministratorMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
	
	private AdministratorMessage getAdminMessageData() {
		String userId = "A1003";
		String senderId = "S1001";
		String senderName = "name";
		LocalDateTime sentAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		String messageText = "Some info for you";
		AdministratorMessage message = new AdministratorMessage();
		
		message.setUserId(userId);
		message.setSenderId(senderId);
		message.setSenderName(senderName);
		message.setSentAt(sentAt);
		message.setMessage(messageText);
		message.setIsRead(false);
		
		return message;
	}
}
