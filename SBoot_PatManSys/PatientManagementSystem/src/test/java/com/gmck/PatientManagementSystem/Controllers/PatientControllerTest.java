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
import com.gmck.PatientManagementSystem.Views.PatientView;

@SpringBootTest
class PatientControllerTest {

	String userId = "P1001";
	
	@Spy
	private PatientView view;
	@Spy
	private StrategySelectFactory strategy;

	@Autowired
	private PatientController instance;

	@Test
	void testSetView() {
		setUser(userId);
		
		assertThat(UIManager.getLookAndFeel().getName()).isEqualTo("FlatLaf Darcula");
		assertThat(view).isNotNull();
		
		PatientController spy = Mockito.spy(instance);
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
			Field instanceView = PatientController.class.getDeclaredField("view");
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
		
		PatientView spy = Mockito.spy(view);
		
		spy.addDeleteMessageButtonHandler(impl);
		verify(spy).addDeleteMessageButtonHandler(impl);
		
		spy.addRateDoctorButtonHandler(impl);
		verify(spy).addRateDoctorButtonHandler(impl);
		
		spy.addRequestAppointmentButtonHandler(impl);
		verify(spy).addRequestAppointmentButtonHandler(impl);
		
		spy.addAccountTerminationButtonHandler(impl);
		verify(spy).addAccountTerminationButtonHandler(impl);
	}

	@Test
	void testSetLogoutHandler() {
		ActionListenerImpl impl = new ActionListenerImpl();		
		instance.setView();
		
		instance.setLogoutHandler();
		
		PatientView spy = Mockito.spy(view);
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
	void testSetAppointmentHistory() {
		String[] appointments = new String[5];
		
		instance.setAppointmentHistory();
		
		PatientView spy = Mockito.spy(view);
		spy.setAppointmentHistory(appointments);
		verify(spy).setAppointmentHistory(appointments);
	}

	@Test
	void testSetPrescriptionHistory() {	
		String[] prescriptions = new String[5];
		
		instance.setPrescriptionHistory();
		
		PatientView spy = Mockito.spy(view);
		spy.setPrescriptionHistory(prescriptions);
		verify(spy).setPrescriptionHistory(prescriptions);
	}

	@Test
	void testSetNotesHistory() {
		String[] notes = new String[5];
		
		instance.setNotesHistory();
		
		PatientView spy = Mockito.spy(view);
		spy.setNotesHistory(notes);
		verify(spy).setNotesHistory(notes);
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
			user = PatientController.class.getDeclaredField("loggedInUserId");
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
