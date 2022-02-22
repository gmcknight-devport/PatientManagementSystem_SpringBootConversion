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

import com.gmck.PatientManagementSystem.Messaging.Entities.DoctorMessage;
import com.gmck.PatientManagementSystem.Messaging.Repositories.DoctorMessageRepo;
import com.gmck.PatientManagementSystem.UserModel.UserType;

@ExtendWith(MockitoExtension.class)
class DoctorMessageServiceTest {

	@Mock
	private DoctorMessageRepo docRepo;
	@Mock 
	DoctorMessage docMessage;
	
	@InjectMocks
	@Autowired
	private DoctorMessageService instance;
	
	@Test
	void createMessage() {		
		DoctorMessage message = getDocMessageData();		
		instance.createMessage(message.getUserId(), message.getSenderId(), message.getSenderName(), message.getSentAt(), message.getMessage());
		
		ArgumentCaptor<DoctorMessage> argCaptor = ArgumentCaptor.forClass(DoctorMessage.class);
		verify(docRepo).save(argCaptor.capture());
		
		DoctorMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
		
	@Test
	void deleteMessageById() {
		DoctorMessage message = getDocMessageData();	
		Long id = message.getId();
		
		Mockito.doReturn(true)
		.when(docRepo)
		.existsById(id);
		
		instance.deleteMessage(id);
		
		verify(docRepo, times(1)).deleteById(id);		
	}
	
	@Test
	void deleteMessage() {
		DoctorMessage message = getDocMessageData();		
		
		instance.deleteMessage(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		verify(docRepo, times(1)).delete(any());		
	}
		
	@Test
	void getUserMessages() {
		DoctorMessage[] obj = new DoctorMessage[1];
		obj[0] = getDocMessageData();
		List<DoctorMessage> message = (List<DoctorMessage>)Arrays.asList(obj);
		
		Mockito.doReturn(message)
		.when(docRepo)
		.getMessagesById(message.get(0).getUserId());
		
		List<DoctorMessage> allUserMessages = instance.getUserMessages(message.get(0).getUserId());
		DoctorMessage expMessage = message.get(0);		
		
		assertThat(allUserMessages.get(0)).usingRecursiveComparison().isEqualTo(expMessage);
	}
		
	@Test
	void messageRead() {
		DoctorMessage message = getDocMessageData();		
		
		Mockito.doReturn(message)
		.when(docRepo)
		.getSpecificMessage(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		instance.messageRead(message.getUserId(), message.getSenderId(), message.getSentAt());
		
		ArgumentCaptor<DoctorMessage> argCaptor = ArgumentCaptor.forClass(DoctorMessage.class);
		verify(docRepo).save(argCaptor.capture());
		
		DoctorMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage.getIsRead()).isEqualTo(message.getIsRead());		
	}
	
	@Test
	void saveMessage() {	
		DoctorMessage message = getDocMessageData();
		instance.createMessage(message.getUserId(), message.getSenderId(), message.getSenderName(), 
				message.getSentAt(), message.getMessage());
		
		ArgumentCaptor<DoctorMessage> argCaptor = ArgumentCaptor.forClass(DoctorMessage.class);
		verify(docRepo).save(argCaptor.capture());
		
		DoctorMessage capturedMessage = argCaptor.getValue();
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(message);
	}
	
	@Test
	void getType() {
		UserType expResult = UserType.D;
		UserType result = instance.getType();
		
		assertThat(result).isEqualTo(expResult);
	}

	private DoctorMessage getDocMessageData() {
		String userId = "D1003";
		String senderId = "A1001";
		String senderName = "name";
		LocalDateTime sentAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		String messageText = "Some info for you";
		DoctorMessage message = new DoctorMessage();
		
		message.setUserId(userId);
		message.setSenderId(senderId);
		message.setSenderName(senderName);
		message.setSentAt(sentAt);
		message.setMessage(messageText);
		message.setIsRead(false);
		
		return message;
	}
}
