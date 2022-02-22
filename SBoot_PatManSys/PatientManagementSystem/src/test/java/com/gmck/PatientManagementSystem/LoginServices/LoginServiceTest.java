package com.gmck.PatientManagementSystem.LoginServices;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gmck.PatientManagementSystem.UserModel.Entities.Address;
import com.gmck.PatientManagementSystem.UserModel.Entities.Doctor;
import com.gmck.PatientManagementSystem.UserModel.Repositories.DoctorRepo;
import com.gmck.PatientManagementSystem.UserModel.Repositories.PatientRepo;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

	@Mock
	private DoctorRepo repo;
	
	@Mock
	private PatientRepo patRepo;
		
	private LoginService instance;
	
	@BeforeEach
	void setUp() {
		instance = new LoginService(null, repo, patRepo, null);
		try {
			Method init = LoginService.class.getDeclaredMethod("initMyServiceCache");
			init.setAccessible(true);
			
			init.invoke(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterEach
	void tearDown() {
		instance = null;
		assertThat(instance).isNull();
	}
	
	@Test
	void testAuthenticateUser() {
		Doctor doc = getDocData().get(0);
		String userId = doc.getUserId();
		char[] password = "password".toCharArray();
		boolean expResult = true;
		boolean result;
		
		mockDoctorId(doc);
		
		result = instance.authenticateUser(userId, password);
		
		assertThat(result).isEqualTo(expResult);		
	}
	
	@Test
	void testAuthenticateUser_nonexistentUser() {
		Doctor doc = getDocData().get(0);
		String userId = "D9999";
		char[] password = "password".toCharArray();		
		boolean expResult = false;
		boolean result;
		
		mockDoctorId(doc);
		
		result = instance.authenticateUser(userId, password);
		
		assertThat(result).isEqualTo(expResult);	
	}
	
	@Test
	void testAuthenticateUser_incorrectPassword() {
		Doctor doc = getDocData().get(1);
		String userId = doc.getUserId();
		char[] password = "wrongpass".toCharArray();
		boolean expResult = false;
		boolean result;
		
		mockDoctorId(doc);
		
		result = instance.authenticateUser(userId, password);
		
		assertThat(result).isEqualTo(expResult);
	}
	
	@Test
	void testAuthenticateUser_invalidUserType() {
		String userId = "E1001";
		char[] password = "password2".toCharArray();
		boolean expResult = false;
		boolean result;
		
		result = instance.authenticateUser(userId, password);
		
		assertThat(result).isEqualTo(expResult);
	}
	
	private List<Doctor> getDocData() {
		List<Doctor> docList = new ArrayList<>();
		
		String doctorId1 = "D1004";
		String doctorId2 = "D1005";
		
		Address surgeryAddress = new Address(doctorId1, "3 Avensis House", "40 Some Road", "Exeter", "EX1 3RT");		
		Doctor doctor1 = new Doctor(doctorId1, "password".toCharArray(), "Mr.", "Phil", "Someone", surgeryAddress, new ArrayList<>());
		surgeryAddress = new Address(doctorId2, "3 Avensis House", "40 Some Road", "Exeter", "EX1 3RT");
		Doctor doctor2 = new Doctor(doctorId2, "password2".toCharArray(), "Miss", "Elena", "Else", surgeryAddress, new ArrayList<>());
		
		docList.add(doctor1);
		docList.add(doctor2);
		
		return docList;
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
