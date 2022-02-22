package com.gmck.PatientManagementSystem.Appointment.Repositories;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gmck.PatientManagementSystem.Appointment.Appointment;

@DataJpaTest
class AppointmentRepoTest {
		
	@Autowired
	private AppointmentRepo instance;
		
	@AfterEach
	void tearDown() {
		instance.deleteAll();
	}
	
	@Test
	void testGetDoctorAppointments() {
		List<Appointment> appointList = getAppointmentTestData();
		List<Appointment> expResult = new ArrayList<>();
		List<Appointment> result = new ArrayList<>();
		String doctorId = "D1003";
				
		for(Appointment a : appointList) {
			if(a.getDoctorId() == doctorId && a.isApproved() == true && a.getAppointmentDate().isAfter(LocalDate.now())) {
				expResult.add(a);
			}
		}
		
		result = instance.getDoctorAppointments(doctorId, LocalDate.now(), true, null);
		
		assertThat(result).hasSize(expResult.size()).hasSameElementsAs(expResult);
	}

	@Test
	void testGetPatientAppointmentsApproved() {
		List<Appointment> appointList = getAppointmentTestData();
		List<Appointment> expResult = new ArrayList<>();
		List<Appointment> result = new ArrayList<>();
		String patientId = "P1003";
		boolean approved = true;
				
		for(Appointment a : appointList) {
			if(a.getPatientId() == patientId && a.getAppointmentDate().isAfter(LocalDate.now()) && a.isApproved() == true) {
				expResult.add(a);
			}
		}
		
		result = instance.getPatientAppointments(patientId, LocalDate.now(), approved, null);
		
		assertThat(result).hasSize(expResult.size()).hasSameElementsAs(expResult);
	}
	
	@Test
	void testGetPatientAppointmentsNotApproved() {
		List<Appointment> appointList = getAppointmentTestData();
		List<Appointment> expResult = new ArrayList<>();
		List<Appointment> result = new ArrayList<>();
		String patientId = "P1003";
		boolean approved = false;
				
		for(Appointment a : appointList) {
			if(a.getPatientId() == patientId && a.isApproved() == false) {
				expResult.add(a);
			}
		}
		
		result = instance.getPatientAppointments(patientId, LocalDate.now(), approved, null);
		
		assertThat(result).hasSize(expResult.size()).hasSameElementsAs(expResult);
	}

	@Test
	void testGetPreviousPatientAppointments() {
		List<Appointment> appointList = getAppointmentTestData();
		List<Appointment> expResult = new ArrayList<>();
		List<Appointment> result = new ArrayList<>();
		String patientId = "P1003";
		boolean approved = true;
				
		for(Appointment a : appointList) {
			if(a.getPatientId() == patientId && a.isApproved() == true && a.getAppointmentDate().isBefore(LocalDate.now())) {
				expResult.add(a);
			}
		}
		
		result = instance.getPreviousPatientAppointments(patientId, LocalDate.now(), approved, null);
		
		assertThat(result).hasSize(expResult.size()).hasSameElementsAs(expResult);
	}

	@Test
	void findByApproved() {
		instance.deleteAll();
		List<Appointment> allAppoints = getAppointmentTestData();
		List<Appointment> expResult = new ArrayList<>();
		List<Appointment> result = new ArrayList<>();
		expResult.add(allAppoints.get(0));
		expResult.add(allAppoints.get(1));
		
		result = instance.findByApproved(true);
		
		assertThat(result).hasSize(expResult.size()).hasSameElementsAs(expResult);
	}
	
	@Test
	void testFindByDoctorIdAndAppointmentDateAndApproved() {
		List<Appointment> appointList = getAppointmentTestData();
		List<Appointment> expResult = new ArrayList<>();
		List<Appointment> result = new ArrayList<>();
		String doctorId = "D1003";
		LocalDate date = LocalDate.now().plusDays(1);
		boolean approved = true;
				
		for(Appointment a : appointList) {
			if(a.getDoctorId() == doctorId && a.getAppointmentDate().equals(date) && a.isApproved() == true) {
				expResult.add(a);
			}
		}
		
		result = instance.findByDoctorIdAndAppointmentDateAndApproved(doctorId, date, approved);
		
		assertThat(result).hasSize(expResult.size()).hasSameElementsAs(expResult);
	}

	@Test
	void testFindByPatientIdAndAppointmentDateAndApproved() {
		List<Appointment> appointList = getAppointmentTestData();
		List<Appointment> expResult = new ArrayList<>();
		List<Appointment> result = new ArrayList<>();
		String patientId = "P1003";
		LocalDate date = LocalDate.now().plusDays(1);
		boolean approved = true;
				
		for(Appointment a : appointList) {
			if(a.getPatientId() == patientId && a.getAppointmentDate().equals(date) && a.isApproved() == true) {
				expResult.add(a);
			}
		}
		
		result = instance.findByPatientIdAndAppointmentDateAndApproved(patientId, date, approved);
		
		assertThat(result).hasSameElementsAs(expResult);
	}

	private List<Appointment> getAppointmentTestData(){
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
		
		instance.save(appoint1);
		instance.save(appoint2);
		instance.save(appoint3);
		
		return appointList;
	}
}
