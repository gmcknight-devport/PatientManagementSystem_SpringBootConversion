package com.gmck.PatientManagementSystem.UserModel.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gmck.PatientManagementSystem.UserModel.Entities.Administrator;

/**
 * Repository for Administrator entity which extends JPA Repository class and adds 
 * an additional method to return more relevant and useful information
 * for operations to be applied to. 
 * @author Glenn McKnight
 *
 */
@Repository
public interface AdministratorRepo extends JpaRepository<Administrator, String>{

	Administrator findFirstByOrderByUserIdDesc();
}
