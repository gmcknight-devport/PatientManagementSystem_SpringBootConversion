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
import com.gmck.PatientManagementSystem.UserModel.Entities.Secretary;
import com.gmck.PatientManagementSystem.UserModel.Repositories.SecretaryRepo;

@ExtendWith(MockitoExtension.class)
class SecretaryServiceTest {

	@Mock
	private SecretaryRepo repo;
	
	@Mock
	private IdService idService;
	
	@InjectMocks
	@Autowired
	private SecretaryService instance; 
	
	@Test
	void testGetUser() {
		Secretary Secretary = getSecData().get(1);
		
		mockSecretaryId(Secretary);		
		Secretary result = instance.getUser(Secretary.getUserId());
		
		assertThat(result).usingRecursiveComparison().isEqualTo(Secretary);
	}

	@Test
	void testGetAllUsers() {
		List<Secretary> expResult = getSecData();
		
		Mockito.doReturn(getSecData())
		.when(repo)
		.findAll();
		
		List<Secretary> result = instance.getAllUsers();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void getUserInfo() {
		List<Secretary> secList = getSecData();
		List<String> expResult = new ArrayList<>();
		
		for(Secretary s : secList) {
			String info = s.getUserId() + ", " + s.getTitle() + " " + s.getForename()
							+ " " + s.getSurname();
			expResult.add(info);
		}
		
		Mockito.doReturn(getSecData())
		.when(repo)
		.findAll();
		
		List<String> result = instance.getUserInfo();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}
	
	@Test
	void testDeleteUser() {
		Secretary Secretary = getSecData().get(1);

		Mockito.doReturn(true)
		.when(repo)
		.existsById(Secretary.getUserId());
		
		instance.deleteUser(Secretary.getUserId());
		
		verify(repo).deleteById(Secretary.getUserId());
	}

	@Test
	void testChangePassword() {
		Secretary secretary = getSecData().get(0);
		char[] password = "NewPass".toCharArray();
		boolean expResult = true;
		boolean result;		
		mockSecretaryId(secretary);
		
		instance.changePassword(secretary.getUserId(), password);		
		result = secretary.authenticate(secretary.getUserId(), password);
		
		assertThat(result).isEqualTo(expResult);
	}

	@Test
	void testUpdateUser_everyValue() {
		Secretary Secretary = getSecData().get(0);
		String newTitle = "Dr";
		String newForename = "Fluid";
		String newSurname = "Naming";
		
		mockSecretaryId(Secretary);
		
		instance.updateUser(Secretary.getUserId(), newTitle, newForename, newSurname);
		
		ArgumentCaptor<Secretary> argCaptor = ArgumentCaptor.forClass(Secretary.class);
		verify(repo).save(argCaptor.capture());
		
		Secretary result = argCaptor.getValue();
		Secretary.setTitle(newTitle);
		Secretary.setForename(newForename);
		Secretary.setSurname(newSurname);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(Secretary);
	}
	
	@Test
	void testUpdateUser_selectedValue() {
		Secretary Secretary = getSecData().get(0);
		String newTitle = "Dr";
		
		mockSecretaryId(Secretary);
		
		instance.updateUser(Secretary.getUserId(), newTitle, null, null);
		
		ArgumentCaptor<Secretary> argCaptor = ArgumentCaptor.forClass(Secretary.class);
		verify(repo).save(argCaptor.capture());
		
		Secretary result = argCaptor.getValue();
		Secretary.setTitle(newTitle);
		
		assertThat(result).usingRecursiveComparison().isEqualTo(Secretary);
	}

	@Test
	void testAddUser() {		
		List<Secretary> secList = getSecData();
		Secretary Secretary = new Secretary("S1004", "opensesame".toCharArray(), "Mrs.", "Lady", "Ladyson");
		
		Mockito.doReturn(secList.get(1))
		.when(repo)
		.findFirstByOrderByUserIdDesc();
		
		Mockito.doReturn("S1004")
		.when(idService)
		.generateId(UserType.S, "S1003");
		
		instance.addUser("opensesame".toCharArray(), Secretary.getTitle(), Secretary.getForename(), Secretary.getSurname());
		
		ArgumentCaptor<Secretary> argCaptor = ArgumentCaptor.forClass(Secretary.class);
		verify(repo).save(argCaptor.capture());
		
		Secretary result = argCaptor.getValue();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(Secretary);
	}
	
	private List<Secretary> getSecData() {
		List<Secretary> secList = new ArrayList<>();
		String secId1 = "S1002";
		String secId2 = "S1003";
		
		Secretary sec1 = new Secretary(secId1, "password".toCharArray(), "Mr.", "Phil", "Someone");
		Secretary sec2 = new Secretary(secId2, "password2".toCharArray(), "Miss", "Elena", "Else");
		
		secList.add(sec1);
		secList.add(sec2);
		
		return secList;
	}
	
	private void mockSecretaryId(Secretary Secretary) {
		Mockito.doReturn(true)
		.when(repo)
		.existsById(Secretary.getUserId());
		
		Mockito.doReturn(Secretary)
		.when(repo)
		.getById(Secretary.getUserId());
	}
}
