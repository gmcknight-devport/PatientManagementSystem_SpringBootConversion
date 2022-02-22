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

import com.gmck.PatientManagementSystem.Messaging.Entities.PatientMessage;

@DataJpaTest
class PatientMessageRepoTest {

	@Autowired
	private PatientMessageRepo instance; 
		
	@AfterEach
	void afterClass() {
		this.instance.deleteAll();
		this.instance = null;
	}
	
	@Test
	void testGetMessagesById() {
		List<PatientMessage> mList = prepareMessageTestData();
		List<PatientMessage> expResult = new ArrayList<>();
		List<PatientMessage> result = new ArrayList<>();
		String userId = "D1003";
				
		for(PatientMessage p : mList) {
			instance.save(p);
			
			if(p.getUserId() == userId) {
				expResult.add(p);
			}
		}

		result = instance.getMessagesById(userId);

		assertThat(result).hasSize(expResult.size()).hasSameElementsAs(expResult);
	}

	@Test
	void testGetSpecificMessage() {
		PatientMessage expResult = prepareMessageTestData().get(0);
		instance.save(expResult);
		
		PatientMessage result = instance.getSpecificMessage(expResult.getUserId(), expResult.getSenderId(), 
				expResult.getSentAt().truncatedTo(ChronoUnit.SECONDS));
		
		assertThat(result).isEqualTo(expResult);
	}
		
	private List<PatientMessage> prepareMessageTestData(){
		List<PatientMessage> messList = new ArrayList<>();
		PatientMessage mess1 = new PatientMessage();
		PatientMessage mess2 = new PatientMessage();
				
		mess1.setUserId("P1003");
		mess1.setSenderId("D1003");
		mess1.setSenderName("Name");
		mess1.setSentAt(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
		mess1.setMessage("Some message here");
		mess1.setIsRead(false);
		
		mess2.setUserId("P1003");
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
