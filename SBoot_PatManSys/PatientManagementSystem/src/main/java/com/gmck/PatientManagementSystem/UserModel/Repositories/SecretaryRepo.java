package com.gmck.PatientManagementSystem.UserModel.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gmck.PatientManagementSystem.UserModel.Entities.Secretary;

/**
 * Repository for Secretary entity which extends JPA Repository class and adds 
 * an additional method to return more relevant and useful information
 * for operations to be applied to. 
 * @author Glenn McKnight
 *
 */
@Repository
public interface SecretaryRepo extends JpaRepository<Secretary, String> {

	Secretary findFirstByOrderByUserIdDesc();
}
