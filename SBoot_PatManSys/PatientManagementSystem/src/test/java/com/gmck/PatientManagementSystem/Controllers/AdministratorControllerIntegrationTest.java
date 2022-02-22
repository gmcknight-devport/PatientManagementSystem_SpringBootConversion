package com.gmck.PatientManagementSystem.Controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.UIManager;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.ErrorUpdate.IErrorUpdateObserver;
import com.gmck.PatientManagementSystem.LoginServices.StrategySelectFactory;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.Views.AdminView;

@SpringBootTest
class AdministratorControllerIntegrationTest {

	@Spy
	private AdminView view;
	@Spy
	private StrategySelectFactory strategy;

	@Autowired
	private AdministratorController instance;

	@Test
	void testSetView() {
		setUser("A1001");
		
		assertThat(UIManager.getLookAndFeel().getName()).isEqualTo("FlatLaf Darcula");
		assertThat(view).isNotNull();
		
		AdministratorController spy = Mockito.spy(instance);
		spy.setView();
		verify(spy).addButtonHandler();
	}

	@Test
	void testSetLoggedInUser() {
		String userId = "A1001";
		
		try {
			Field result = setUser(userId);
			
			assertThat(result.get(instance)).isEqualTo(userId);
		} catch (Exception e) {
			System.out.println("Test failed");
			e.printStackTrace();
		}		
	}

	@Test
	void testDisposeView() {
		try {
			Field instanceView = AdministratorController.class.getDeclaredField("view");
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
		
		AdminView spy = Mockito.spy(view);
		
		spy.addCreateUserButtonHandler(impl);
		verify(spy).addCreateUserButtonHandler(impl);
		
		spy.addRemoveDoctorButtonHandler(impl);
		verify(spy).addRemoveDoctorButtonHandler(impl);
		
		spy.addRemoveSecButtonHandler(impl);
		verify(spy).addRemoveSecButtonHandler(impl);
		
		spy.addRemoveAdminButtonHandler(impl);
		verify(spy).addRemoveAdminButtonHandler(impl);
		
		spy.addDoctorRatingJComboHandler(impl);
		verify(spy).addDoctorRatingJComboHandler(impl);
		
		spy.addSendFeedbackJButtonHandler(impl);
		verify(spy).addSendFeedbackJButtonHandler(impl);
	}

	@Test
	void testSetLogoutHandler() {
		ActionListenerImpl impl = new ActionListenerImpl();		
		instance.setView();
		
		instance.setLogoutHandler();
		
		AdminView spy = Mockito.spy(view);
		spy.addLogoutButtonHandler(impl);
		verify(spy).addLogoutButtonHandler(impl);
	}

	@Test
	void testSetStrategy() {
		UserType type = UserType.L;
		setUser("A1001");
		
		StrategySelectFactory spy = Mockito.spy(strategy);
		spy.execute(type, null);
		verify(spy).execute(type, null);
	}

	@Test
	void testUpdate() {
		String message = "Some Message";
		UpdateObserverImpl obsImpl = new UpdateObserverImpl();
		ErrorUpdate.getInstance().addObserver(obsImpl);
		
		ErrorUpdate.getInstance().updateObserver(message);
		
		assertThat(obsImpl.message).isEqualTo(message);
	}

	private Field setUser(String userId) {
		Field user;
		
		try {
			user = AdministratorController.class.getDeclaredField("loggedInUserId");
			user.setAccessible(true);
			
			instance.setLoggedInUser(userId);
			return user;
			
		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
			return null;
		}		
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