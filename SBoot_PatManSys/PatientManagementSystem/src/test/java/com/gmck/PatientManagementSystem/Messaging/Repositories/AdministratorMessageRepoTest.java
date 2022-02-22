package com.gmck.PatientManagementSystem.Messaging.Repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gmck.PatientManagementSystem.Messaging.Entities.AdministratorMessage;

@DataJpaTest
class AdministratorMessageRepoTest {

	@Autowired
	private AdministratorMessageRepo instance; 
		
	@AfterEach
	void afterClass() {
		this.instance.deleteAll();
		this.instance = null;
	}
	
	@Test
	void testGetMessagesById() {
		List<AdministratorMessage> mList = prepareMessageTestData();
		List<AdministratorMessage> expResult = new ArrayList<>();
		List<AdministratorMessage> result = new ArrayList<>();
		String userId = "A1001";
				
		for(AdministratorMessage a : mList) {
			instance.save(a);
			
			if(a.getUserId() == userId) {
				expResult.add(a);
			}
		}

		result = instance.getMessagesById(userId);

		assertThat(result).hasSize(expResult.size()).hasSameElementsAs(expResult);
	}

	@Test
	void testGetSpecificMessage() {
		AdministratorMessage expResult = prepareMessageTestData().get(0);
		instance.save(expResult);
		
		AdministratorMessage result = instance.getSpecificMessage(expResult.getUserId(), expResult.getSenderId(), 
				expResult.getSentAt().truncatedTo(ChronoUnit.SECONDS));
		
		assertThat(result).isEqualTo(expResult);
	}
		
	private List<AdministratorMessage> prepareMessageTestData(){
		List<AdministratorMessage> messList = new ArrayList<>();
		AdministratorMessage mess1 = new AdministratorMessage();
		AdministratorMessage mess2 = new AdministratorMessage();
				
		mess1.setUserId("A1001");
		mess1.setSenderId("D1001");
		mess1.setSenderName("Name");
		mess1.setSentAt(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
		mess1.setMessage("Some message here");
		mess1.setIsRead(false);
		
		mess2.setUserId("A1001");
		mess2.setSenderId("S1001");
		mess2.setSenderName("Name");
		mess2.setSentAt(LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.SECONDS));
		mess2.setMessage("Information for you");
		mess2.setIsRead(false);
				
		messList.add(mess1);
		messList.add(mess2);
				
		return messList;
	}
}
