package com.gmck.PatientManagementSystem.LoginServices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.gmck.PatientManagementSystem.Controllers.DoctorController;
import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.UserModel.UserType;

@ExtendWith(MockitoExtension.class)
class DoctorStrategyTest {

	@Mock
	private DoctorController controller;
	
	@Mock
	private ErrorUpdate update;
	
	@InjectMocks
	@Autowired
	private DoctorStrategy instance;
		
	@Test
	void testSetLoggedInUser() {
		String userId = "S9999";
		instance.execute(userId);
		
		verify(controller).setLoggedInUser(userId);
	}

	@Test
	void testSetObserver() throws RuntimeException{
		try (MockedStatic<ErrorUpdate> mockStatic = Mockito.mockStatic(ErrorUpdate.class)){
			
			mockStatic.when(() -> ErrorUpdate.getInstance()).thenReturn(update);
			instance.setObserver();
			
			verify(update).addObserver(controller);
			verify(update).removeAllObservers();
		}
	}

	@Test
	void testSetView() {
		instance.setView();
		verify(controller).setView();
	}

	@Test
	void testGetType() {
		UserType expResult = UserType.D;
		UserType result = instance.getType();
		
		assertThat(result).isEqualTo(expResult);
	}
}
