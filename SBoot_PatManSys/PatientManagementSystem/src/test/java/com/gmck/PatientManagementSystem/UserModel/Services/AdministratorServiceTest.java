package com.gmck.PatientManagementSystem.UserModel.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
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
import com.gmck.PatientManagementSystem.UserModel.Entities.Administrator;
import com.gmck.PatientManagementSystem.UserModel.Repositories.AdministratorRepo;

@ExtendWith(MockitoExtension.class)
class AdministratorServiceTest {

	@Mock
	private AdministratorRepo repo;
	
	@Mock
	private IdService idService;
	
	@InjectMocks
	@Autowired
	private AdministratorService instance; 
	
	@Test
	void testGetUser() {
		Administrator administrator = getAdminData().get(1);
		
		mockAdministratorId(administrator);		
		Administrator result = instance.getUser(administrator.getUserId());
		
		assertThat(result).usingRecursiveComparison().isEqualTo(administrator);
	}

	@Test
	void testGetAllUsers() {
		List<Administrator> expResult = getAdminData();
		
		Mockito.doReturn(getAdminData())
		.when(repo)
		.findAll();
		
		List<Administrator> result = instance.getAllUsers();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void getUserInfo() {
		List<Administrator> adminList = getAdminData();
		List<String> expResult = new ArrayList<>();
		
		for(Administrator a : adminList) {
			String info = a.getUserId() + ", " + a.getTitle() + " " + a.getForename()
							+ " " + a.getSurname();
			expResult.add(info);
		}
		
		Mockito.doReturn(getAdminData())
		.when(repo)
		.findAll();
		
		List<String> result = instance.getUserInfo();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}
	
	@Test
	void testDeleteUser() {
		List<Administrator> adminList = getAdminData();
		String userId = adminList.get(0).getUserId();

		Mockito.doReturn(true)
		.when(repo)
		.existsById(userId);
		
		Mockito.doReturn(adminList)
		.when(repo)
		.findAll();
		
		instance.deleteUser(userId);
		
		verify(repo).deleteById(userId);
	}
	
	@Test
	void testDeleteUser_failsMustHaveOneAdmin() {
		Administrator administrator = getAdminData().get(1);

		Mockito.doReturn(true)
		.when(repo)
		.existsById(administrator.getUserId());
				
		instance.deleteUser(administrator.getUserId());
		
		verify(repo, never()).deleteById(administrator.getUserId());
	}

	@Test
	void testChangePassword() {
		Administrator admin = getAdminData().get(0);
		char[] password = "NewPass".toCharArray();
		boolean expResult = true;
		boolean result;		
		mockAdministratorId(admin);
		
		instance.changePassword(admin.getUserId(), password);		
		result = admin.authenticate(admin.getUserId(), password);
		
		assertThat(result).isEqualTo(expResult);
	}

	@Test
	void testUpdateUser_everyValue() {
		Administrator administrator = getAdminData().get(0);
		String newTitle = "Dr";
		String newForename = "Fluid";
		String newSurname = "Naming";
		
		mockAdministratorId(administrator);
		
		instance.updateUser(administrator.getUserId(), newTitle, newForename, newSurname);
		
		ArgumentCaptor<Administrator> argCaptor = ArgumentCaptor.forClass(Administrator.class);
		verify(repo).save(argCaptor.capture());
		
		Administrator result = argCaptor.getValue();
		administrator.setTitle(newTitle);
		administrator.setForename(newForename);
		administrator.setSurname(newSurname);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(administrator);
	}
	
	@Test
	void testUpdateUser_selectedValue() {
		Administrator administrator = getAdminData().get(0);
		String newTitle = "Dr";
		
		mockAdministratorId(administrator);
		
		instance.updateUser(administrator.getUserId(), newTitle, null, null);
		
		ArgumentCaptor<Administrator> argCaptor = ArgumentCaptor.forClass(Administrator.class);
		verify(repo).save(argCaptor.capture());
		
		Administrator result = argCaptor.getValue();
		administrator.setTitle(newTitle);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(administrator);
	}

	@Test
	void testAddUser() {		
		List<Administrator> adminList = getAdminData();
		Administrator administrator = new Administrator("A1004", "opensesame".toCharArray(), "Mrs.", "Lady", "Ladyson");
		
		Mockito.doReturn(adminList.get(1))
		.when(repo)
		.findFirstByOrderByUserIdDesc();
		
		Mockito.doReturn("A1004")
		.when(idService)
		.generateId(UserType.A, "A1003");
		
		instance.addUser("opensesame".toCharArray(), administrator.getTitle(), administrator.getForename(), administrator.getSurname());
		
		ArgumentCaptor<Administrator> argCaptor = ArgumentCaptor.forClass(Administrator.class);
		verify(repo).save(argCaptor.capture());
		
		Administrator result = argCaptor.getValue();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(administrator);
	}
	
	private List<Administrator> getAdminData() {
		List<Administrator> adminList = new ArrayList<>();
		String adminId1 = "A1002";
		String adminId2 = "A1003";
		
		Administrator admin1 = new Administrator(adminId1, "password".toCharArray(), "Mr.", "Phil", "Someone");
		Administrator admin2 = new Administrator(adminId2, "password2".toCharArray(), "Miss", "Elena", "Else");
		
		adminList.add(admin1);
		adminList.add(admin2);
		
		return adminList;
	}
	
	private void mockAdministratorId(Administrator administrator) {
		Mockito.doReturn(true)
		.when(repo)
		.existsById(administrator.getUserId());
		
		Mockito.doReturn(administrator)
		.when(repo)
		.getById(administrator.getUserId());
	}
}
