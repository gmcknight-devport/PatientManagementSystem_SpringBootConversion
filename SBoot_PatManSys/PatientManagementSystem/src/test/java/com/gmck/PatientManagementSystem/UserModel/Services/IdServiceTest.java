package com.gmck.PatientManagementSystem.UserModel.Services;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gmck.PatientManagementSystem.UserModel.UserType;

class IdServiceTest {

	private IdService instance; 
	
	@BeforeEach
	void setUp() throws Exception {
		instance = new IdService();
	}

	@AfterEach
	void tearDown() throws Exception {
		instance = null;
		assertThat(instance).isNull();
	}

	@Test
	void testGenerateId_correctId() {
		UserType type = UserType.D;
		String highestId = "D1003";
		String expResult = "D1004";
		
		String result = instance.generateId(type, highestId);
		
		assertThat(result).isEqualTo(expResult);
	}
	
	@Test
	void testGenerateId_invalidId() {
		UserType type = UserType.D;
		String highestId = "DDDDD";
		
		//String result = instance.generateId(type, highestId);
		
		//assertThat(result).isNull();
		
		assertThatThrownBy(() -> instance.generateId(type, highestId))
		.isInstanceOf(NumberFormatException.class);
	}
	
	@Test
	void testGnerateId_incorrectLength() {
		UserType type = UserType.S;
		String highestId = "S101";
		
		assertThatThrownBy(() -> instance.generateId(type, highestId))
		.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void testGnerateId_nullValue() {
		UserType type = UserType.A;
		String highestId = null;
			
		assertThatThrownBy(() -> instance.generateId(type, highestId))
		.isInstanceOf(NullPointerException.class);
	}

	@Test
	void testGnerateId_returnDefault() {
		UserType type = UserType.A;
		String highestId = "";
		String expResult = "A1001";
		
		String result = instance.generateId(type, highestId);
				
		assertThat(result).isEqualTo(expResult);
	}	
}
