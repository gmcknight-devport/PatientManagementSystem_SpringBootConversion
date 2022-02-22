package com.gmck.PatientManagementSystem.UserModel.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

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

import com.gmck.PatientManagementSystem.UserModel.UserType;
import com.gmck.PatientManagementSystem.UserModel.Entities.Address;
import com.gmck.PatientManagementSystem.UserModel.Entities.Doctor;
import com.gmck.PatientManagementSystem.UserModel.Repositories.DoctorRepo;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

	@Mock
	private DoctorRepo repo;
	
	@Mock
	private IdService idService;
	
	@InjectMocks
	@Autowired
	private DoctorService instance;
	
	@Test
	void testGetUser() {
		Doctor doctor = getDoctorData().get(1);
		
		mockDoctorId(doctor);		
		Doctor result = instance.getUser(doctor.getUserId());
		
		assertThat(result).usingRecursiveComparison().isEqualTo(doctor);
	}

	@Test
	void testGetAllUsers() {
		List<Doctor> expResult = getDoctorData();
		
		Mockito.doReturn(getDoctorData())
		.when(repo)
		.findAll();
		
		List<Doctor> result = instance.getAllUsers();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void getUserInfo() {
		List<Doctor> docList = getDoctorData();
		List<String> expResult = new ArrayList<>();
		
		for(Doctor d : docList) {
			String info = d.getUserId() + ", " + d.getTitle() + " " + d.getForename()
							+ " " + d.getSurname();
			expResult.add(info);
		}
		
		Mockito.doReturn(getDoctorData())
		.when(repo)
		.findAll();
		
		List<String> result = instance.getUserInfo();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}
	
	@Test
	void testDeleteUser() {
		Doctor doctor = getDoctorData().get(1);

		Mockito.doReturn(true)
		.when(repo)
		.existsById(doctor.getUserId());
		
		instance.deleteUser(doctor.getUserId());
		
		verify(repo).deleteById(doctor.getUserId());
	}

	@Test
	void testChangePassword() {
		Doctor doctor = getDoctorData().get(0);
		char[] password = "NewPass".toCharArray();
		boolean expResult = true;
		boolean result;		
		mockDoctorId(doctor);
		
		instance.changePassword(doctor.getUserId(), password);		
		result = doctor.authenticate(doctor.getUserId(), password);
		
		assertThat(result).isEqualTo(expResult);
	}

	@Test
	void testUpdateUser_everyValue() {
		Doctor doctor = getDoctorData().get(0);
		String newTitle = "Dr";
		String newForename = "Fluid";
		String newSurname = "Naming";
		
		mockDoctorId(doctor);
		
		instance.updateUser(doctor.getUserId(), newTitle, newForename, newSurname);
		
		ArgumentCaptor<Doctor> argCaptor = ArgumentCaptor.forClass(Doctor.class);
		verify(repo).save(argCaptor.capture());
		
		Doctor result = argCaptor.getValue();
		doctor.setTitle(newTitle);
		doctor.setForename(newForename);
		doctor.setSurname(newSurname);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(doctor);
	}
	
	@Test
	void testUpdateUser_selectedValue() {
		Doctor doctor = getDoctorData().get(0);
		String newTitle = "Dr";
		
		mockDoctorId(doctor);
		
		instance.updateUser(doctor.getUserId(), newTitle, null, null);
		
		ArgumentCaptor<Doctor> argCaptor = ArgumentCaptor.forClass(Doctor.class);
		verify(repo).save(argCaptor.capture());
		
		Doctor result = argCaptor.getValue();
		doctor.setTitle(newTitle);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(doctor);
	}

	@Test
	void testAddUser() {		
		List<Doctor> doctorList = getDoctorData();
		Address surgeryAddress = new Address("D1006", "3 Avensis House", "40 Some Road", "Exeter", "EX1 3RT");
		Doctor doctor = new Doctor("D1006", "opensesame".toCharArray(), "Mrs.", "Lady", "Ladyson", surgeryAddress, new ArrayList<Double>());
		
		Mockito.doReturn(doctorList.get(1))
		.when(repo)
		.findFirstByOrderByUserIdDesc();
		
		Mockito.doReturn("D1006")
		.when(idService)
		.generateId(UserType.D, "D1005");
		
		instance.addUser("opensesame".toCharArray(), doctor.getTitle(), doctor.getForename(), doctor.getSurname(), 
				doctor.getSurgeryAddress().getAddressLine1(), doctor.getSurgeryAddress().getAddressLine2(), doctor.getSurgeryAddress().getCity(),
				doctor.getSurgeryAddress().getPostcode());
		
		ArgumentCaptor<Doctor> argCaptor = ArgumentCaptor.forClass(Doctor.class);
		verify(repo).save(argCaptor.capture());
		
		Doctor result = argCaptor.getValue();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(doctor);
	}

	@Test
	void testUpdateAddress() {
		Doctor doctor = getDoctorData().get(0);
		Address address = new Address(doctor.getUserId(), "4000", "New Address", "Exeter", "EX2 3TY");
		
		mockDoctorId(doctor);
		
		instance.updateAddress(address.getUserId(), address.getAddressLine1(), address.getAddressLine2(), 
				address.getCity(), address.getPostcode());
		
		ArgumentCaptor<Doctor> argCaptor = ArgumentCaptor.forClass(Doctor.class);
		verify(repo).save(argCaptor.capture());
		
		Address result = argCaptor.getValue().getSurgeryAddress();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(address);
	}

	
	@Test
	void testGetDoctorRatings() {
		Doctor doctor = getDoctorData().get(0);
		String docId = doctor.getUserId();
		int rating1 = 5;
		int rating2 = 4;
		List<Double> expResult = new ArrayList<>();
		expResult.add((double)rating1);
		expResult.add((double)rating2);
				
		doctor.addRating(rating1);
		doctor.addRating(rating2);
		
		Mockito.doReturn(doctor)
		.when(repo)
		.getById(docId);
		
		List<Double> result = instance.getDoctorRatings(docId);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}
	
	@Test
	void testGetDoctorsWithRatings() {
		List<Doctor> docList = getDoctorData();
		int rating1 = 5;
		int rating2 = 4;
		double average = ((double)rating1 + (double)rating2) / 2;
		List<String> expResult = new ArrayList<>();
		expResult.add(docList.get(0).getUserId() + ", " + docList.get(0).getForename() + ", " + docList.get(0).getSurname() + ", " + average);
		expResult.add(docList.get(1).getUserId() + ", " + docList.get(1).getForename() + ", " + docList.get(1).getSurname() + ", " + 0.0);
		
		docList.get(0).addRating(rating1);
		docList.get(0).addRating(rating2);
		
		mockDoctorId(docList.get(0));
		mockDoctorId(docList.get(1));
		
		Mockito.doReturn(docList)
		.when(repo)
		.findAll();		
		
		List<String> result = instance.getDoctorsWithRatings();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testAddRating() {
		Doctor doctor = getDoctorData().get(0);
		int rating = 5;
		
		mockDoctorId(doctor);
		
		instance.addRating(doctor.getUserId(), rating);
		
		assertThat(doctor.getRatings().get(0)).isEqualTo(rating);
	}
	
	private List<Doctor> getDoctorData() {
		List<Doctor> doctorList = new ArrayList<>();
		String doctorId1 = "D1004";
		String doctorId2 = "D1005";
		
		Address surgeryAddress = new Address(doctorId1, "3 Avensis House", "40 Some Road", "Exeter", "EX1 3RT");		
		Doctor doctor1 = new Doctor(doctorId1, "password".toCharArray(), "Mr.", "Phil", "Someone", surgeryAddress, new ArrayList<>());
		surgeryAddress = new Address(doctorId2, "3 Avensis House", "40 Some Road", "Exeter", "EX1 3RT");
		Doctor doctor2 = new Doctor(doctorId2, "password2".toCharArray(), "Miss", "Elena", "Else", surgeryAddress, new ArrayList<>());
		
		doctorList.add(doctor1);
		doctorList.add(doctor2);
		
		return doctorList;
	}
	
	private void mockDoctorId(Doctor doctor) {
		Mockito.doReturn(true)
		.when(repo)
		.existsById(doctor.getUserId());
		
		Mockito.doReturn(doctor)
		.when(repo)
		.getById(doctor.getUserId());
	}
}
