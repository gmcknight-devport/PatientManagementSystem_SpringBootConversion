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
import com.gmck.PatientManagementSystem.Views.DoctorView;

@SpringBootTest
class DoctorControllerIntegrationTest {
	
	String userId = "D1001";
	
	@Spy
	private DoctorView view;
	@Spy
	private StrategySelectFactory strategy;

	@Autowired
	private DoctorController instance;

	@Test
	void testSetView() {
		setUser(userId);
		
		assertThat(UIManager.getLookAndFeel().getName()).isEqualTo("FlatLaf Darcula");
		assertThat(view).isNotNull();
		
		DoctorController spy = Mockito.spy(instance);
		spy.setView();
		verify(spy).addButtonHandler();
	}

	@Test
	void testSetLoggedInUser() {
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
			Field instanceView = DoctorController.class.getDeclaredField("view");
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
		
		DoctorView spy = Mockito.spy(view);
		
		spy.addCreateAppointmentButtonHandler(impl);
		verify(spy).addCreateAppointmentButtonHandler(impl);
		
		spy.addPatientJComboListener(impl);
		verify(spy).addPatientJComboListener(impl);
		
		spy.addDeleteMessageButtonHandler(impl);
		verify(spy).addDeleteMessageButtonHandler(impl);
		
		spy.addNotesButtonHandler(impl);
		verify(spy).addNotesButtonHandler(impl);
		
		spy.addPrescribeButtonHandler(impl);
		verify(spy).addPrescribeButtonHandler(impl);
		
		spy.addCreateMedicineButtonHandler(impl);
		verify(spy).addCreateMedicineButtonHandler(impl);
	}

	@Test
	void testSetLogoutHandler() {
		ActionListenerImpl impl = new ActionListenerImpl();		
		instance.setView();
		
		instance.setLogoutHandler();
		
		DoctorView spy = Mockito.spy(view);
		spy.addLogoutButtonHandler(impl);
		verify(spy).addLogoutButtonHandler(impl);
	}

	@Test
	void testSetStrategy() {
		UserType type = UserType.L;
		setUser(userId);
		
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
			user = DoctorController.class.getDeclaredField("loggedInUserId");
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
