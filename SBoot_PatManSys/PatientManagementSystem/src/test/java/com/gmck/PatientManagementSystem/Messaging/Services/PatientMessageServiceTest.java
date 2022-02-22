package com.gmck.PatientManagementSystem.Messaging.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

import com.gmck.PatientManagementSystem.Messaging.Entities.PatientMessage;
import com.gmck.PatientManagementSystem.Messaging.Repositories.PatientMessageRepo;
import com.gmck.PatientManagementSystem.UserModel.UserType;

@ExtendWith(MockitoExtension.class)
class PatientMessageServiceTest {

	@Mock
	private PatientMessageRepo patRepo;
	@Mock
	private PatientMessage patMessage;
	
	@InjectMocks
	@Autowired
	private PatientMessageService instance;
	
	@Test
	void createMessage() {		
		PatientMessage message = getPatMessageData();		
		instance.createMessage(message.getUserId(), message.getSenderId(), message.getSenderName(), message.getSentAt(), message.getMessage());
		
		ArgumentCaptor<PatientMessage> argCaptor = ArgumentCaptor.forClass(PatientMessage.class);
		verify(patRepo).save(argCaptor.capture());
		
		PatientMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
		
	@Test
	void deleteMessageById() {
		PatientMessage message = getPatMessageData();	
		Long id = message.getId();
		
		Mockito.doReturn(true)
		.when(patRepo)
		.existsById(id);
		
		instance.deleteMessage(id);
		
		verify(patRepo, times(1)).deleteById(id);		
	}
	
	@Test
	void deleteMessage() {
		PatientMessage message = getPatMessageData();		
		
		instance.deleteMessage(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		verify(patRepo, times(1)).delete(any());		
	}
		
	@Test
	void getUserMessages() {
		PatientMessage[] obj = new PatientMessage[1];
		obj[0] = getPatMessageData();
		List<PatientMessage> message = (List<PatientMessage>)Arrays.asList(obj);
		
		Mockito.doReturn(message)
		.when(patRepo)
		.getMessagesById(message.get(0).getUserId());
		
		List<PatientMessage> allUserMessages = instance.getUserMessages(message.get(0).getUserId());
		PatientMessage expMessage = message.get(0);		
		
		assertThat(allUserMessages.get(0)).usingRecursiveComparison().isEqualTo(expMessage);
	}
		
	@Test
	void messageRead() {
		PatientMessage message = getPatMessageData();		
		
		Mockito.doReturn(message)
		.when(patRepo)
		.getSpecificMessage(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		instance.messageRead(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		ArgumentCaptor<PatientMessage> argCaptor = ArgumentCaptor.forClass(PatientMessage.class);
		verify(patRepo).save(argCaptor.capture());
		
		PatientMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage.getIsRead()).isEqualTo(message.getIsRead());		
	}
	
	@Test
	void saveMessage() {	
		PatientMessage message = getPatMessageData();
		instance.createMessage(message.getUserId(), message.getSenderId(), message.getSenderName(), 
				message.getSentAt(), message.getMessage());
		
		ArgumentCaptor<PatientMessage> argCaptor = ArgumentCaptor.forClass(PatientMessage.class);
		verify(patRepo).save(argCaptor.capture());
		
		PatientMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
	
	@Test
	void getType() {
		UserType expResult = UserType.P;
		UserType result = instance.getType();
		
		assertThat(result).isEqualTo(expResult);
	}
	
	private PatientMessage getPatMessageData() {
		String userId = "P1003";
		String senderId = "D1003";
		String senderName = "name";
		LocalDateTime sentAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		String messageText = "Some info for you";
		PatientMessage message = new PatientMessage();
		
		message.setUserId(userId);
		message.setSenderId(senderId);
		message.setSenderName(senderName);
		message.setSentAt(sentAt);
		message.setMessage(messageText);
		message.setIsRead(false);
		
		return message;
	}
}
