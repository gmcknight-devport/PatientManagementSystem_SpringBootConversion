package com.gmck.PatientManagementSystem.UserModel.Entities;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientTest {

	Patient instance;
		
	@BeforeEach
	public void init() {
		instance = new Patient();
	}
	
	@AfterEach
	public void tearDown() {
		instance = null;
		assertThat(instance).isNull();
	}
	
	@Test
	void setGender_allowedValue() {
		char gender = 'F';
			
		instance.setGender(gender);			
		assertThat(instance.getGender()).isEqualTo(gender);
	}

	/*
	 * Should return default value
	 */
	@Test
	void setGender_disallowedValue() {
		char gender = 'L';
		char expResult = 'M';
		
		instance.setGender(gender);
		assertThat(instance.getGender()).isEqualTo(expResult);
	}	
}
