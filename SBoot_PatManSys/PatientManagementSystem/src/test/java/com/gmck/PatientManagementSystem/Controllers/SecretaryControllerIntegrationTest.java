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
import com.gmck.PatientManagementSystem.Views.SecretaryView;

@SpringBootTest
class SecretaryControllerIntegrationTest {
	
	String userId = "S1001";
	
	@Spy
	private SecretaryView view;
	@Spy
	private StrategySelectFactory strategy;

	@Autowired
	private SecretaryController instance;

	@Test
	void testSetView() {
		setUser(userId);
		
		assertThat(UIManager.getLookAndFeel().getName()).isEqualTo("FlatLaf Darcula");
		assertThat(view).isNotNull();
		
		SecretaryController spy = Mockito.spy(instance);
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
			Field instanceView = SecretaryController.class.getDeclaredField("view");
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
		
		SecretaryView spy = Mockito.spy(view);
		
		spy.addDeleteMessageButtonHandler(impl);
		verify(spy).addDeleteMessageButtonHandler(impl);
		
		spy.addApproveAccountButtonHandler(impl);
		verify(spy).addApproveAccountButtonHandler(impl);
		
		spy.addDeclineAccountButtonHandler(impl);
		verify(spy).addDeclineAccountButtonHandler(impl);
		
		spy.addRemoveAccountButtonHandler(impl);
		verify(spy).addRemoveAccountButtonHandler(impl);
		
		spy.addApproveAppointmentButtonHandler(impl);
		verify(spy).addApproveAppointmentButtonHandler(impl);
		
		spy.addDeclineAppointmentButtonHandler(impl);
		verify(spy).addDeclineAppointmentButtonHandler(impl);
		
		spy.addCreateAppointmentButtonHandler(impl);
		verify(spy).addCreateAppointmentButtonHandler(impl);
       
		spy.addDeleteAppointmentButtonHandler(impl);
		verify(spy).addDeleteAppointmentButtonHandler(impl);
		
		spy.addPrescribeMedicineButtonHandler(impl);
		verify(spy).addPrescribeMedicineButtonHandler(impl);
		
		spy.addOrderMedicineButtonHandler(impl);
		verify(spy).addOrderMedicineButtonHandler(impl);
		
		spy.addDeleteMedicineButtonHandler(impl);
		verify(spy).addDeleteMedicineButtonHandler(impl);
		
		spy.addAppointmentPatIdChangeHandler(impl);
		verify(spy).addAppointmentPatIdChangeHandler(impl);
	}

	@Test
	void testSetLogoutHandler() {
		ActionListenerImpl impl = new ActionListenerImpl();		
		instance.setView();
		
		instance.setLogoutHandler();
		
		SecretaryView spy = Mockito.spy(view);
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
			user = SecretaryController.class.getDeclaredField("loggedInUserId");
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
