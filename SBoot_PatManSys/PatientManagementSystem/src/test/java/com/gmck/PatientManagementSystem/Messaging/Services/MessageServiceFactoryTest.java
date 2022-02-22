package com.gmck.PatientManagementSystem.Messaging.Services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MessageServiceFactoryTest {
	
	@SuppressWarnings("static-access")
	@Test
	void testGetMessageService_allowedValue() {
		String userId = "P1001";
		MessageServiceFactory mock = Mockito.mock(MessageServiceFactory.class);
		
		verify(mock, times(1)).getMessageService(userId);;
	}
	
	@Test
	void testGetMessageService_invalidUsertype() {
		String userId = "H1001";
				
		try {
			MessageServiceFactory.getMessageService(userId);
		}catch(RuntimeException e) {
			assertThat(e).isInstanceOf(RuntimeException.class).hasMessage("Invalid value for enum UserType: H");
		}
	}	
}
