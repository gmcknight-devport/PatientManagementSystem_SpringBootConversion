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
import com.gmck.PatientManagementSystem.UserModel.Entities.TempUser;
import com.gmck.PatientManagementSystem.UserModel.Repositories.TempUserRepo;

@ExtendWith(MockitoExtension.class)
class TempUserServiceTest {

	@Mock
	private TempUserRepo repo;
	
	@Mock
	private IdService idService;
	
	@InjectMocks
	@Autowired
	private TempUserService instance;
	
	@Test
	void testGetUser() {
		TempUser tempUser = getTempUserData().get(1);
		
		mockTempUserId(tempUser);		
		TempUser result = instance.getUser(tempUser.getUserId());
		
		assertThat(result).usingRecursiveComparison().isEqualTo(tempUser);
	}

	@Test
	void testGetAllUsers() {
		List<TempUser> expResult = getTempUserData();
		
		Mockito.doReturn(getTempUserData())
		.when(repo)
		.findAll();
		
		List<TempUser> result = instance.getAllUsers();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(expResult);
	}

	@Test
	void testDeleteUser() {
		TempUser tempUser = getTempUserData().get(1);

		Mockito.doReturn(true)
		.when(repo)
		.existsById(tempUser.getUserId());
		
		instance.deleteUser(tempUser.getUserId());
		
		verify(repo).deleteById(tempUser.getUserId());
	}

	@Test
	void testAddUser() {		
		List<TempUser> userList = getTempUserData();
		Address address = new Address("T1003", "1 road", "Village", "Villington", "PL1 3EE");
		TempUser tempUser = new TempUser("T1003", "password".toCharArray(), "Mrs", "Lady", "Ladyson", 30, address, 'F');
		
		Mockito.doReturn(userList.get(1))
		.when(repo)
		.findFirstByOrderByUserIdDesc();
		
		Mockito.doReturn("T1003")
		.when(idService)
		.generateId(UserType.T, "T1002");
		
		instance.addUser("password".toCharArray(), tempUser.getTitle(), tempUser.getForename(), tempUser.getSurname(), 
				tempUser.getAge(), tempUser.getAddress().getAddressLine1(), tempUser.getAddress().getAddressLine2(), tempUser.getAddress().getCity(), 
				tempUser.getAddress().getPostcode());
		
		ArgumentCaptor<TempUser> argCaptor = ArgumentCaptor.forClass(TempUser.class);
		verify(repo).save(argCaptor.capture());
		
		TempUser result = argCaptor.getValue();
		
		assertThat(result).usingRecursiveComparison().isEqualTo(tempUser);
	}
	
	private List<TempUser> getTempUserData(){
		
		List<TempUser> tempUserList = new ArrayList<>();
		String userId1 = "T1001";
		String userId2 = "T1002";
		
		Address address1 = new Address(userId1, "55 this street", "", "Bolton", "BL67 8YT");
		Address address2 = new Address(userId2, "1 some place road", "Addres Line 2", "Plymouth", "PL3 5TY");
		
		TempUser temp1 = new TempUser(userId1, "password".toCharArray(), "Mr", "Phil", "Someone", 30, address1, 'M');
		TempUser temp2 = new TempUser(userId2, "password2".toCharArray(), "Mrs", "Elena", "Else", 50, address2, 'F');
		
		tempUserList.add(temp1);
		tempUserList.add(temp2);
			
		return tempUserList;
	}
	
	private void mockTempUserId(TempUser user) {
		Mockito.doReturn(true)
		.when(repo)
		.existsById(user.getUserId());
		
		Mockito.doReturn(user)
		.when(repo)
		.getById(user.getUserId());
	}
}
