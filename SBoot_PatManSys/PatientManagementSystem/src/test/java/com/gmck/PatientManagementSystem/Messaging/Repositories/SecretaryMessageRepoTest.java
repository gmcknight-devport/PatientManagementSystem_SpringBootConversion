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

import com.gmck.PatientManagementSystem.Messaging.Entities.SecretaryMessage;

@DataJpaTest
class SecretaryMessageRepoTest {

	@Autowired
	private SecretaryMessageRepo instance; 
		
	@AfterEach
	void afterClass() {
		this.instance.deleteAll();
		this.instance = null;
	}
	
	@Test
	void testGetMessagesById() {
		List<SecretaryMessage> mList = prepareMessageTestData();
		List<SecretaryMessage> expResult = new ArrayList<>();
		List<SecretaryMessage> result = new ArrayList<>();
		String userId = "D1003";
				
		for(SecretaryMessage s : mList) {
			instance.save(s);
			
			if(s.getUserId() == userId) {
				expResult.add(s);
			}
		}

		result = instance.getMessagesById(userId);

		assertThat(result).hasSize(expResult.size()).hasSameElementsAs(expResult);
	}

	@Test
	void testGetSpecificMessage() {
		SecretaryMessage expResult = prepareMessageTestData().get(0);
		instance.save(expResult);
		
		SecretaryMessage result = instance.getSpecificMessage(expResult.getUserId(), expResult.getSenderId(), 
				expResult.getSentAt().truncatedTo(ChronoUnit.SECONDS));
		
		assertThat(result).isEqualTo(expResult);
	}
		
	private List<SecretaryMessage> prepareMessageTestData(){
		List<SecretaryMessage> messList = new ArrayList<>();
		SecretaryMessage mess1 = new SecretaryMessage();
		SecretaryMessage mess2 = new SecretaryMessage();
				
		mess1.setUserId("S1001");
		mess1.setSenderId("D1003");
		mess1.setSenderName("Name");
		mess1.setSentAt(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
		mess1.setMessage("Some message here");
		mess1.setIsRead(false);
		
		mess2.setUserId("S1001");
		mess2.setSenderId("D1003");
		mess2.setSenderName("Name");
		mess2.setSentAt(LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.SECONDS));
		mess2.setMessage("Information for you");
		mess2.setIsRead(false);
				
		messList.add(mess1);
		messList.add(mess2);
				
		return messList;
	}

}
