package com.gmck.PatientManagementSystem.Messaging.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.gmck.PatientManagementSystem.Messaging.Entities.SecretaryMessage;
import com.gmck.PatientManagementSystem.Messaging.Repositories.SecretaryMessageRepo;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Secretary;
import com.gmck.PatientManagementSystem.UserModel.Services.SecretaryService;

@ExtendWith(MockitoExtension.class)
class SecretaryMessageServiceTest {

	@Mock
	private SecretaryMessageRepo secRepo;
	@Mock
	private SecretaryMessage secMessage;
	@Mock
	private SecretaryService secService;
	
	@InjectMocks
	@Autowired
	private SecretaryMessageService instance;

	@Test
	void createMessage() {		
		SecretaryMessage message = getSecMessageData();		
		instance.createMessage(message.getUserId(), message.getSenderId(), message.getSenderName(), message.getSentAt(), message.getMessage());
		
		ArgumentCaptor<SecretaryMessage> argCaptor = ArgumentCaptor.forClass(SecretaryMessage.class);
		verify(secRepo).save(argCaptor.capture());
		
		SecretaryMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
		
	@Test
	void deleteMessageById() {
		SecretaryMessage message = getSecMessageData();	
		Long id = message.getId();
		
		Mockito.doReturn(true)
		.when(secRepo)
		.existsById(id);
		
		instance.deleteMessage(id);
		
		verify(secRepo, times(1)).deleteById(id);		
	}
	
	@Test
	void deleteMessage() {
		SecretaryMessage message = getSecMessageData();		
		
		instance.deleteMessage(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		verify(secRepo, times(1)).delete(any());		
	}
		
	@Test
	void getUserMessages() {
		SecretaryMessage[] obj = new SecretaryMessage[1];
		obj[0] = getSecMessageData();
		List<SecretaryMessage> message = (List<SecretaryMessage>)Arrays.asList(obj);
		
		Mockito.doReturn(message)
		.when(secRepo)
		.getMessagesById(message.get(0).getUserId());
		
		List<SecretaryMessage> allUserMessages = instance.getUserMessages(message.get(0).getUserId());
		SecretaryMessage expMessage = message.get(0);		
		
		assertThat(allUserMessages.get(0)).usingRecursiveComparison().isEqualTo(expMessage);
	}
		
	@Test
	void messageRead() {
		SecretaryMessage message = getSecMessageData();		
		
		Mockito.doReturn(message)
		.when(secRepo)
		.getSpecificMessage(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		instance.messageRead(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		ArgumentCaptor<SecretaryMessage> argCaptor = ArgumentCaptor.forClass(SecretaryMessage.class);
		verify(secRepo).save(argCaptor.capture());
		
		SecretaryMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage.getIsRead()).isEqualTo(message.getIsRead());		
	}
	
	@Test
	void saveMessage() {	
		SecretaryMessage message = getSecMessageData();
		instance.createMessage(message.getUserId(), message.getSenderId(), message.getSenderName(), 
				message.getSentAt(), message.getMessage());
		
		ArgumentCaptor<SecretaryMessage> argCaptor = ArgumentCaptor.forClass(SecretaryMessage.class);
		verify(secRepo).save(argCaptor.capture());
		
		SecretaryMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
	
	@Test
	void getType() {
		UserType expResult = UserType.S;
		UserType result = instance.getType();
		
		assertThat(result).isEqualTo(expResult);
	}
	
	@Test
	void createMessageToAll() {	
		List<Secretary> secList = new ArrayList<>();
		Secretary sec = new Secretary("S1003", "password".toCharArray(), "Mrs.", "Missy", "Missington");
		secList.add(sec);
		SecretaryMessage message = getSecMessageData();		
		
		Mockito.doReturn(secList)
		.when(secService)
		.getAllUsers();	
		
		instance.createMessageToAll(message.getSenderId(), message.getSenderName(), message.getSentAt(), message.getMessage());
		
		ArgumentCaptor<SecretaryMessage> argCaptor = ArgumentCaptor.forClass(SecretaryMessage.class);
		verify(secRepo).save(argCaptor.capture());
		
		SecretaryMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
	
	private SecretaryMessage getSecMessageData() {
		String userId = "S1003";
		String senderId = "P1003";
		String senderName = "name";
		LocalDateTime sentAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		String messageText = "Some info for you";
		SecretaryMessage message = new SecretaryMessage();
		
		message.setUserId(userId);
		message.setSenderId(senderId);
		message.setSenderName(senderName);
		message.setSentAt(sentAt);
		message.setMessage(messageText);
		message.setIsRead(false);
		
		return message;
	}
}
