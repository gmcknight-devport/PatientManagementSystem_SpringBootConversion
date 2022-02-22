package com.gmck.PatientManagementSystem.Medicine.Services;

import java.time.LocalDate;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.Medicine.Medicine;
import com.gmck.PatientManagementSystem.Medicine.Repositories.MedicineRepo;
import com.gmck.PatientManagementSystem.UserModel.Services.PatientService;

/**
 * Concrete implementation of the IMedicineService interface. 
 * Performs operations on Medicine entities and interacts with the repository 
 * storing them.
 * @author Glenn McKnight
 *
 */
@Service
public class MedicineService implements IMedicineService {

	private MedicineRepo medRepo;	
	private PatientService patService;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
	 */
	@Autowired
	public MedicineService(MedicineRepo medRepo, PatientService patService) {
		this.medRepo = medRepo;
		this.patService = patService;
	}
	
	@Override
	public Medicine getMedicine(String medName) {
		Medicine medicine = null;
		
		if(medRepo.existsById(medName)) {
			medicine = medRepo.getById(medName);
		}else {
			ErrorUpdate.getInstance().updateObserver("Couldn't find medicine");
		}
		
		return medicine;
	}
	
	@Override
	public List<Medicine> getAllMedicines(){
		return medRepo.findAll();
	}
		
	@Override
	public void prescribeMedicine(String patientId, String doctorId, String medName, int quantity) {//String dosage, String commonUses, int quantity) {		
		int quantUpdate;
		Medicine medicine = medRepo.getById(medName);
		String prescription = medName + ". " + medicine.getMedDosage() + ". " + medicine.getCommonUses() + ". " + Integer.toString(quantity) +".";

		patService.addPatientPrescription(patientId, prescription, doctorId, LocalDate.now());
		
		quantUpdate = medicine.getQuantity() - quantity;
		medicine.setQuantity(quantUpdate);
		
		saveMedicine(medicine);
	}
	
	@Override
	public void addMedicine(String medName, String dosage, String commonUses) {
		Medicine medicine = new Medicine();
		medicine.setMedName(medName);
		medicine.setMedDosage(dosage);
		medicine.setCommonUses(commonUses);
		medicine.setQuantity(0);
		
		saveMedicine(medicine);
	}
	
	@Override
	public void removeMedicine(String medName) {
		medRepo.deleteById(medName);
	}
	
	@Override
	public void orderMedicine(String medName, int quantity) {
		Medicine medicine = medRepo.getById(medName);
		int quantUpdate = medicine.getQuantity() + quantity;
		medicine.setQuantity(quantUpdate);
		
		saveMedicine(medicine);
	}
	
	@Override
	public void saveMedicine(Medicine medicine) {
		try {
			medRepo.save(medicine);
			
		}catch(ConstraintViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Medicine does not conform to validation rules - ensure there aren't "
					+ "null or incorrectly formatted values");
		}catch(DataIntegrityViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Referenced foreign key field doesn't exist");
		}catch(IllegalArgumentException e) {
			ErrorUpdate.getInstance().updateObserver("Medicine contains null values");
		}
	}
}
