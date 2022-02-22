package com.gmck.PatientManagementSystem.LoginServices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.gmck.PatientManagementSystem.UserModel.UserType;

@ExtendWith(MockitoExtension.class)
class StrategySelectFactoryTest {
	
	@Mock
	private PatientStrategy strategy;
	
	@InjectMocks
	@Autowired
	private StrategySelectFactory instance;
	
	@Test
	void testGetMessageService_allowedValue() {
		String userId = "P1001";		
		StrategySelectFactory mock = Mockito.mock(StrategySelectFactory.class);	
		
		mock.execute(UserType.P, userId);
		
		verify(mock, times(1)).execute(UserType.P, userId);
	}
	
	@Test
	void testGetMessageService_invalidUsertype() {
		String userId = "H1001";
				
		try {
			instance.execute(UserType.valueOf(userId.substring(0, 1)), userId);
		}catch(RuntimeException e) {
			assertThat(e).isInstanceOf(RuntimeException.class);
		}
	}	
}
