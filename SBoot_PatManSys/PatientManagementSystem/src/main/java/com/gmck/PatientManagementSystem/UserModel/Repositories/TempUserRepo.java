package com.gmck.PatientManagementSystem.UserModel.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gmck.PatientManagementSystem.UserModel.Entities.TempUser;

/**
 * Repository for TempUser entity which extends JPA Repository class and adds 
 * an additional method to return more relevant and useful information
 * for operations to be applied to. 
 * @author Glenn McKnight
 *
 */
@Repository
public interface TempUserRepo extends JpaRepository<TempUser, String>{
	
	TempUser findFirstByOrderByUserIdDesc();
}
