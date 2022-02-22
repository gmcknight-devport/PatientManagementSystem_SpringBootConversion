package com.gmck.PatientManagementSystem.Messaging.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gmck.PatientManagementSystem.Messaging.Entities.AdministratorMessage;

/**
 * Repository for AdministratorMessage entity which extends JPA Repository class and adds 
 * additional methods to return more relevant and useful information
 * for operations to be applied to. 
 * @author Glenn McKnight
 *
 */
public interface AdministratorMessageRepo extends JpaRepository<AdministratorMessage, Long> {

	
	@Query(value = "SELECT m FROM AdministratorMessage m WHERE m.userId = ?1")
	List<AdministratorMessage> getMessagesById(String userId);
	
	
	@Query(value = "SELECT m FROM AdministratorMessage m WHERE m.userId = ?1 AND m.senderId = ?2 AND m.sentAt = ?3")
	AdministratorMessage getSpecificMessage(String userId, String senderId, LocalDateTime sentAt);	
}
