package com.gmck.PatientManagementSystem.Controllers;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.UIManager;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.ErrorUpdate.IErrorUpdateObserver;
import com.gmck.PatientManagementSystem.LoginServices.LoginService;
import com.gmck.PatientManagementSystem.LoginServices.StrategySelectFactory;
import com.gmck.PatientManagementSystem.UserModel.Services.TempUserService;
import com.gmck.PatientManagementSystem.Views.LoginView;

@SpringBootTest
class LoginControllerIntegrationTest {

	@Spy
	private LoginView view;
	private LoginService loginService;
	private StrategySelectFactory strategy;
	private TempUserService tempService;

	private LoginController instance;

	@BeforeEach
	void setUp() {
		instance = new LoginController(loginService, strategy, tempService);
	}
	
	@AfterEach
	void tearDown() {
		instance = null;
		assertThat(instance).isNull();
	}
	
	@Test
	void testInit() {
		String message = "Message";
		UpdateObserverImpl obsImpl = new UpdateObserverImpl();
		ErrorUpdate.getInstance().addObserver(obsImpl);
				
		ErrorUpdate.getInstance().updateObserver(message);
		assertThat(obsImpl.message).isEqualTo(message);
				
		LoginController spy = Mockito.spy(instance);
		spy.init();
		verify(spy).setView();
	}

	@Test
	void testSetView() {
		assertThat(UIManager.getLookAndFeel().getName()).isEqualTo("FlatLaf Darcula");
		assertThat(view).isNotNull();
		
		LoginController spy = Mockito.spy(instance);
		spy.setView();
		verify(spy).addButtonHandler();
	}

	@Test
	void testSetLoggedInUser() {
		String userId = "S1001";
		
		try {
			Field result = LoginController.class.getDeclaredField("loggedInUser");
			result.setAccessible(true);
			
			instance.setLoggedInUser(userId);
			
			assertThat(result.get(instance)).isEqualTo(userId);
		} catch (Exception e) {
			System.out.println("Test failed");
			e.printStackTrace();
		}		
	}

	@Test
	void testDisposeView() {
		try {
			Field instanceView = LoginController.class.getDeclaredField("view");
			instanceView.setAccessible(true);
			
			instance.setView();
			instance.disposeView();
			
			assertThat(instanceView.get(instance)).isNull();
		} catch (Exception e) {
			System.out.println("Test failed");
			e.printStackTrace();
		}		
	}

	@Test
	void testAddButtonHandler() {
		ActionListenerImpl impl = new ActionListenerImpl();		
		instance.setView();
		
		instance.addButtonHandler();
		
		LoginView spy = Mockito.spy(view);
		spy.addLoginButtonHandler(impl);
		verify(spy).addLoginButtonHandler(impl);
		
		spy.addSignupButtonHandler(impl);
		verify(spy).addSignupButtonHandler(impl);
	}

	@Test
	void testUpdate() {
		String message = "Some Message";
		UpdateObserverImpl obsImpl = new UpdateObserverImpl();
		ErrorUpdate.getInstance().addObserver(obsImpl);
		
		ErrorUpdate.getInstance().updateObserver(message);
		
		assertThat(obsImpl.message).isEqualTo(message);
	}
	
	class UpdateObserverImpl implements IErrorUpdateObserver{

		public String message;
		
		@Override
		public void update(String errorMessage) {
			this.message = errorMessage;	
		}	
	}

	class ActionListenerImpl implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			throw new NotYetImplementedException();		
		}	
	}
}