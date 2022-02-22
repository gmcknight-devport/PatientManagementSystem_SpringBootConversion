package com.gmck.PatientManagementSystem.UserModel.Services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.gmck.PatientManagementSystem.Appointment.Appointment;
import com.gmck.PatientManagementSystem.Appointment.Services.AppointmentService;
import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Address;
import com.gmck.PatientManagementSystem.UserModel.Entities.Patient;
import com.gmck.PatientManagementSystem.UserModel.Entities.PatientPrescription;
import com.gmck.PatientManagementSystem.UserModel.Repositories.PatientRepo;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

	@Mock
	private PatientRepo repo;
	
	@Mock
	private IdService idService;
	
	@Mock 
	private AppointmentService appointService;
	
	@InjectMocks
	@Autowired
	private PatientService instance; 
	
	@Test
	void testGetUser() {
		Patient patient = getPatData().get(1);
		
		mockPatientId(patient);		
		Patient result = instance.getUser(patient.getUserId());
		
		assertThat(result).usingRecursiveComparison().isEqualTo(patient);
	}

	@Test
	void testGetAllUsers() {
		List<Patient> expResult = getPatData();
		
		Mockito.doReturn(getPatData())
		.when(repo)
		.findAll();
		
		List<Patient> result = instance.getAllUsers();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void getUserInfo() {
		List<Patient> patList = getPatData();
		List<String> expResult = new ArrayList<>();
		
		for(Patient p : patList) {
			String info = p.getUserId() + ", " + p.getTitle() + " " + p.getForename()
							+ " " + p.getSurname();
			expResult.add(info);
		}
		
		Mockito.doReturn(getPatData())
		.when(repo)
		.findAll();
		
		List<String> result = instance.getUserInfo();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}
	
	@Test
	void testDeleteUser() {
		Patient patient = getPatData().get(1);
		List<Appointment> patAppoint = new ArrayList<>();

		Mockito.doReturn(true)
		.when(repo)
		.existsById(patient.getUserId());
		
		Mockito.doReturn(patAppoint)
		.when(appointService)
		.getPatientAppointments(patient.getUserId());
		
		instance.deleteUser(patient.getUserId());
		
		verify(repo).deleteById(patient.getUserId());
	}

	@Test
	void testChangePassword() {
		Patient patient = getPatData().get(0);
		char[] password = "NewPass".toCharArray();
		boolean expResult = true;
		boolean result;		
		mockPatientId(patient);
		
		instance.changePassword(patient.getUserId(), password);		
		result = patient.authenticate(patient.getUserId(), password);
		
		assertThat(result).isEqualTo(expResult);
	}

	@Test
	void testUpdateUser_everyValue() {
		Patient patient = getPatData().get(0);
		String newTitle = "Dr";
		String newForename = "Fluid";
		String newSurname = "Naming";
		
		mockPatientId(patient);
		
		instance.updateUser(patient.getUserId(), newTitle, newForename, newSurname);
		
		ArgumentCaptor<Patient> argCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(repo).save(argCaptor.capture());
		
		Patient result = argCaptor.getValue();
		patient.setTitle(newTitle);
		patient.setForename(newForename);
		patient.setSurname(newSurname);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(patient);
	}
	
	@Test
	void testUpdateUser_selectedValue() {
		Patient patient = getPatData().get(0);
		String newTitle = "Dr";
		
		mockPatientId(patient);
		
		instance.updateUser(patient.getUserId(), newTitle, null, null);
		
		ArgumentCaptor<Patient> argCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(repo).save(argCaptor.capture());
		
		Patient result = argCaptor.getValue();
		patient.setTitle(newTitle);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(patient);
	}

	@Test
	void testAddUser() {		
		List<Patient> patList = getPatData();
		Address address = new Address("P1006", "1 road", "Village", "Villington", "PL1 3EE");
		Patient patient = new Patient("P1006", "opensesame".toCharArray(), "Mrs.", "Lady", "Ladyson", 30, address, 'M', 
				new ArrayList<>(), new ArrayList<>());
		
		Mockito.doReturn(patList.get(1))
		.when(repo)
		.findFirstByOrderByUserIdDesc();
		
		Mockito.doReturn("P1006")
		.when(idService)
		.generateId(UserType.P, "P1005");
		
		instance.addUser("opensesame".toCharArray(), patient.getTitle(), patient.getForename(), patient.getSurname(), 
				patient.getAge(), patient.getAddress().getAddressLine1(), patient.getAddress().getAddressLine2(), patient.getAddress().getCity(), 
				patient.getAddress().getPostcode(), patient.getGender());
		
		ArgumentCaptor<Patient> argCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(repo).save(argCaptor.capture());
		
		Patient result = argCaptor.getValue();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(patient);
	}

	@Test
	void testUpdateAge() {
		Patient patient = getPatData().get(0);
		int age = 100; 
		
		mockPatientId(patient);
		
		instance.updateAge(patient.getUserId(), age);
		
		ArgumentCaptor<Patient> argCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(repo).save(argCaptor.capture());
		
		int result = argCaptor.getValue().getAge();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(age);
	}

	@Test
	void testUpdateGender() {
		Patient patient = getPatData().get(0);
		char gender = 'O';
		
		mockPatientId(patient);
		
		instance.updateGender(patient.getUserId(), gender);
		
		ArgumentCaptor<Patient> argCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(repo).save(argCaptor.capture());
		
		char result = argCaptor.getValue().getGender();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(gender);
	}

	@Test
	void testAddPatientNotes() {
		Patient patient = getPatData().get(0);
		String notes = "New notes about this patient";
		List<String> expResult = new ArrayList<>();
		expResult.add(notes);
		
		mockPatientId(patient);
		
		instance.addPatientNotes(patient.getUserId(), notes);
		
		ArgumentCaptor<Patient> argCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(repo).save(argCaptor.capture());
		
		List<String> result = argCaptor.getValue().getNotes();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testUpdateAddress() {
		Patient patient = getPatData().get(0);
		Address address = new Address(patient.getUserId(), "4000", "New Address", "Exeter", "EX2 3TY");
		
		mockPatientId(patient);
		
		instance.updateAddress(address.getUserId(), address.getAddressLine1(), address.getAddressLine2(), 
				address.getCity(), address.getPostcode());
		
		ArgumentCaptor<Patient> argCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(repo).save(argCaptor.capture());
		
		Address result = argCaptor.getValue().getAddress();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(address);
	}

	@Test
	void testAddPatientPrescription() {
		Patient patient = getPatData().get(0);
		String docId = "D1004";
		String prescription = "Medicine. dosed at, 6. Many ailments. 2 provided";
		List<PatientPrescription> expResult = new ArrayList<>();
		expResult.add(new PatientPrescription(patient.getUserId(), prescription, docId, LocalDate.now()));
		
		mockPatientId(patient);
		
		instance.addPatientPrescription(patient.getUserId(), prescription, docId, LocalDate.now());
		
		ArgumentCaptor<Patient> argCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(repo).save(argCaptor.capture());
		
		List<PatientPrescription> result = argCaptor.getValue().getPrescriptions();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	private List<Patient> getPatData() {
		List<Patient> patList = new ArrayList<>();
		String patId1 = "P1004";
		String patId2 = "P1005";
		
		Address address1 = new Address(patId1, "55 this street", "", "Bolton", "BL67 8YT");
		Address address2 = new Address(patId2, "1 some place road", "Addres Line 2", "Plymouth", "PL3 5TY");
		
		Patient pat1 = new Patient(patId1, "password".toCharArray(), "Mr.", "Phil", "Someone", 30, address1, 'M', 
				new ArrayList<>(), new ArrayList<>());
		Patient pat2 = new Patient(patId2, "password2".toCharArray(), "Miss", "Elena", "Else", 50, address2, 'F',
				new ArrayList<>(), new ArrayList<>());
		
		patList.add(pat1);
		patList.add(pat2);
		
		return patList;
	}
	
	private void mockPatientId(Patient patient) {
		Mockito.doReturn(true)
		.when(repo)
		.existsById(patient.getUserId());
		
		Mockito.doReturn(patient)
		.when(repo)
		.getById(patient.getUserId());
	}
}
