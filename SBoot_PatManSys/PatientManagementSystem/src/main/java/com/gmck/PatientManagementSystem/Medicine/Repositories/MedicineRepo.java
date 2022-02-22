package com.gmck.PatientManagementSystem.Medicine.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmck.PatientManagementSystem.Medicine.Medicine;

/**
 * Repository for Medicine class which extends JPA Repository class
 * @author Glenn McKnight
 *
 */
public interface MedicineRepo extends JpaRepository<Medicine, String> {

}
