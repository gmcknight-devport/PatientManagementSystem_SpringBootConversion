package com.gmck.PatientManagementSystem.Appointment.Services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.gmck.PatientManagementSystem.Appointment.Appointment;
import com.gmck.PatientManagementSystem.Appointment.AppointmentPropertyConfig;
import com.gmck.PatientManagementSystem.Appointment.Repositories.AppointmentRepo;
import com.gmck.PatientManagementSystem.Messaging.Services.PatientMessageService;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

	@Mock
	private AppointmentPropertyConfig config;
	@Mock
	private AppointmentRepo repo;
	@Mock 
	private PatientMessageService patMessageService;
	
	@InjectMocks
	@Autowired
	private AppointmentService instance;
	
	private Sort sort = Sort.by(Sort.Order.desc("appointmentDate"));
		
	@Test
	void testGetPatientAppointments() {
		List<Appointment> appointList = getAppointData();
		List<Appointment> expResult = new ArrayList<>();
		String patientId = appointList.get(0).getPatientId();				

		for(Appointment a : appointList) {
			if(a.getPatientId() == patientId && a.isApproved() == true) {
				expResult.add(a);
			}
		}
		
		Mockito.doReturn(expResult)
		.when(repo)
		.getPatientAppointments(patientId, LocalDate.now(), true, sort);
		
		List<Appointment> result = instance.getPatientAppointments(patientId);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testGetDoctorAppointments() {
		List<Appointment> appointList = getAppointData();
		List<Appointment> expResult = new ArrayList<>();
		String doctorId = appointList.get(0).getPatientId();				

		for(Appointment a : appointList) {
			if(a.getDoctorId() == doctorId && a.isApproved() == true) {
				expResult.add(a);
			}
		}
		
		Mockito.doReturn(expResult)
		.when(repo)
		.getDoctorAppointments(doctorId, LocalDate.now(), true, sort);
		
		List<Appointment> result = instance.getDoctorAppointments(doctorId);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testGetRequestedAppointments_noParams() {
		List<Appointment> appointList = getAppointData();
		List<Appointment> expResult = new ArrayList<>();				

		for(Appointment a : appointList) {
			if(a.isApproved() == false) {
				expResult.add(a);
			}
		}
		
		Mockito.doReturn(expResult)
		.when(repo)
		.findByApproved(false);
		
		List<Appointment> result = instance.getRequestedAppointments();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}
	
	@Test
	void testGetRequestedAppointments_patientParam() {
		List<Appointment> appointList = getAppointData();
		List<Appointment> expResult = new ArrayList<>();
		String patientId = appointList.get(0).getPatientId();				

		for(Appointment a : appointList) {
			if(a.getPatientId() == patientId && a.isApproved() == false) {
				expResult.add(a);
			}
		}
		
		Mockito.doReturn(expResult)
		.when(repo)
		.getPatientAppointments(patientId, LocalDate.now(), false, sort);
		
		List<Appointment> result = instance.getRequestedAppointments(patientId);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testGetPreviousAppointments() {
		List<Appointment> appointList = getAppointData();
		List<Appointment> expResult = new ArrayList<>();
		String patientId = appointList.get(0).getPatientId();				

		for(Appointment a : appointList) {
			if(a.getPatientId() == patientId && a.getAppointmentDate().isBefore(LocalDate.now())) {
				expResult.add(a);
			}
		}
		
		Mockito.doReturn(expResult)
		.when(repo)
		.getPreviousPatientAppointments(patientId, LocalDate.now(), true, sort);
		
		List<Appointment> result = instance.getPreviousAppointments(patientId);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testCreateAppointment() {
		Appointment appointment = getAppointData().get(1);
		appointment.setAppointmentDate(LocalDate.now().plusDays(3));		
		mockAppointConfigReturnTimes();	
		mockAppointConfigReturnDays();	
				
		instance.createAppointment(appointment.getPatientId(), appointment.getDoctorId(), appointment.getStartTime(), 
				appointment.getEndTime(), appointment.getAppointmentDate(), appointment.isApproved());
		
		ArgumentCaptor<Appointment> argCaptor = ArgumentCaptor.forClass(Appointment.class);
		verify(repo).save(argCaptor.capture());
		
		Appointment capturedAppointment = argCaptor.getValue();
		
		assertThat(capturedAppointment).usingRecursiveComparison().isEqualTo(appointment);
	}

	@Test
	void testRequestAppointment_allowedValue() {
		Appointment appointment = getAppointData().get(1);
		appointment.setAppointmentDate(LocalDate.now().plusDays(3));		
		mockAppointConfigReturnTimes();	
		mockAppointConfigReturnDays();	
				
		instance.requestAppointment(appointment.getPatientId(), appointment.getDoctorId(), appointment.getStartTime(), 
				appointment.getEndTime(), appointment.getAppointmentDate());
		
		ArgumentCaptor<Appointment> argCaptor = ArgumentCaptor.forClass(Appointment.class);
		verify(repo).save(argCaptor.capture());
		
		Appointment capturedAppointment = argCaptor.getValue();
		appointment.setApproved(false);
		
		assertThat(capturedAppointment).usingRecursiveComparison().isEqualTo(appointment);
	}
	
//	@Test
//	void testRequestAppointment_doctorUnavailable() {
//		
//		//Return requested arguments 
//		fail("Not yet implemented");
//	}
//	
//	@Test
//	void testRequestAppointment_patientUnavailable() {
//		
//		//Return requested arguments 
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testRequestAppointment_outsideWorkingHours() {
//		
//		//Return requested arguments 
//		fail("Not yet implemented");
//	}
	
	@Test
	void testApproveAppointment() {
		Appointment appointment = getAppointData().get(2);
		long id = 1;
		
		Mockito.doReturn(appointment)
		.when(repo)
		.getById(id);
		
		instance.approveAppointment(id);
		
		ArgumentCaptor<Appointment> argCaptor = ArgumentCaptor.forClass(Appointment.class);
		verify(repo).save(argCaptor.capture());
		
		Appointment capturedMessage = argCaptor.getValue();
		appointment.setApproved(true);
		
		assertThat(capturedMessage).usingRecursiveComparison().isEqualTo(appointment);
	}

	@Test
	void testDeclineAppointment_deleteCalled() {
		Appointment appointment = getAppointData().get(1);
		long id = 1; 
		String senderId = "S1001";
		String senderName = "Some name";
		String message = "Appointment unavailable";
		
		Mockito.doReturn(appointment)
		.when(repo)
		.getById(id);
		
		instance.declineAppointment(id, senderId, senderName, message);		
		verify(repo).delete(appointment);
	}
	
	@Test
	void TestDeclineAppointment_messageSent() {
		Appointment appointment = getAppointData().get(1);
		long id = 1; 
		String userId = appointment.getPatientId();
		String senderId = "S1001";
		String senderName = "Some name";
		String message = "Appointment unavailable";
		LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
		String expResult = message + ", " + appointment.getAppointmentDate() + ", " + appointment.getStartTime() 
		+ "-" + appointment.getEndTime() + " with: " + appointment.getDoctorId() + ".";
		
		Mockito.doReturn(appointment)
		.when(repo)
		.getById(id);
		
		instance.declineAppointment(id, senderId, senderName, message);		
		verify(patMessageService).createMessage(userId, senderId, senderName, date, expResult);
	}

	@Test
	void deleteAppointment() {
		long id = 1; 
		
		instance.deleteAppointment(id);		
		verify(repo).deleteById(id);
	}
	
	@Test
	void testGetPossibleTimes() {
		List<Set<LocalTime>> expResult = getPossTimes();		
		mockAppointConfigReturnTimes();
		
		List<Set<LocalTime>> result = instance.getPossibleTimes();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testSaveAppointment() {
		Appointment appointment = getAppointData().get(0);
		
		instance.saveAppointment(appointment);
		
		verify(repo).save(appointment);
	}

	private List<Appointment> getAppointData() {
		List<Appointment> appointList = new ArrayList<>();
		
		Appointment appoint1 = new Appointment();
		Appointment appoint2 = new Appointment();
		Appointment appoint3 = new Appointment();
		
		appoint1.setPatientId("P1003");
		appoint1.setDoctorId("D1003");
		appoint1.setAppointmentDate(LocalDate.now().plusDays(1));
		appoint1.setStartTime(LocalTime.of(9, 00));
		appoint1.setEndTime(LocalTime.of(9, 30));
		appoint1.setApproved(true);
		
		appoint2.setPatientId("P1003");
		appoint2.setDoctorId("D1003");
		appoint2.setAppointmentDate(LocalDate.now().minusDays(1));
		appoint2.setStartTime(LocalTime.of(11, 30));
		appoint2.setEndTime(LocalTime.of(12, 00));
		appoint2.setApproved(true);
		
		appoint3.setPatientId("P1003");
		appoint3.setDoctorId("D1003");
		appoint3.setAppointmentDate(LocalDate.now().plusDays(10));
		appoint3.setStartTime(LocalTime.of(11, 30));
		appoint3.setEndTime(LocalTime.of(12, 00));
		appoint3.setApproved(false);
		
		appointList.add(appoint1);
		appointList.add(appoint2);
		appointList.add(appoint3);
		
		return appointList;
	}
	
	private List<Set<LocalTime>> getPossTimes(){
		List<Set<LocalTime>> timesList = new ArrayList<>();
		LocalTime startTime = LocalTime.of(8, 0);
		LocalTime endTime = startTime.plusMinutes(30);
		Set<LocalTime> slot;
		
		while(!endTime.equals(LocalTime.of(18, 0))) {
			slot = new TreeSet<>();
			slot.add(startTime);
			slot.add(endTime);
			timesList.add(slot);
			
			startTime = startTime.plusMinutes(30);
			endTime = endTime.plusMinutes(30);
		}
		
		return timesList;
	}
	
	private void mockAppointConfigReturnTimes() {
		LocalTime startTime = LocalTime.of(8, 0);
		LocalTime endTime = LocalTime.of(18, 0);
		
		Mockito.doReturn(startTime)
		.when(config)
		.getOpeningTime();
		
		Mockito.doReturn(endTime)
		.when(config)
		.getClosingTime();
		
		
	}	
	
	private void mockAppointConfigReturnDays() {
		List<String> daysList = new ArrayList<>();
		daysList.add("MONDAY");
		daysList.add("TUESDAY");
		daysList.add("WEDNESDAY");
		daysList.add("THURSDAY");
		daysList.add("FRIDAY");
		
		Mockito.doReturn(daysList)
		.when(config)
		.getDaysOpen();
	}
}
