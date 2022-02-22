package com.gmck.PatientManagementSystem.Medicine.Services;

import java.util.List;

import com.gmck.PatientManagementSystem.Medicine.Medicine;

/**
 * Interface to enforce the implementation of specified methods to ensure proper 
 * working of a medicine service class - allows operations to be carried out on
 * data from repository. 
 * @author Glenn McKnight
 *
 */
public interface IMedicineService {

	/**
	 * Getter for a medicine searched for by the ID parameter.
	 * @param medName - ID of medicine, is unique. 
	 * @return
	 */
	Medicine getMedicine(String medName);
	
	/**
	 * Getter for list of all medicines in the repository.
	 * @return List - of all medicines. 
	 */
	List<Medicine> getAllMedicines();
	
	/**
	 * Prescribe a medicine to a patient. Adds to their prescription history,
	 * updates the quantity in the medicine, and saves to repo. 
	 * @param patientId - The ID of the patient to prescribe too.
	 * @param doctorId - The ID of the doctor prescribing. 
	 * @param medName - The unique name of the medicine.
	 * @param dosage - The recommended dosage.
	 * @param commonUses - The commonUses of the medicine. 
	 * @param quantity - The quantity to be prescribed to the patient. 
	 */
	void prescribeMedicine(String patientId, String doctorId, String medName, int quantity);
	
	/**
	 * Creates a new type of medicine based on the parameters and adds it to the repo. 
	 * @param medName - The unique name for the new medicine. 
	 * @param dosage - The dosage for the new medicine. 
	 * @param commonUses  The common uses for the new medicine. 
	 */
	void addMedicine(String medName, String dosage, String commonUses);
	
	/**
	 * Removes a medicine from the repo using the ID parameter.
	 * @param medName - unique ID of the medicine.
	 */
	void removeMedicine(String medName);
	
	/**
	 * Orders more of a selected medicine based on the parameters. 
	 * @param medName - the ID of the medicine to order more of. 
	 * @param quantity - the amount to order. 
	 */
	void orderMedicine(String medName, int quantity);
	
	/**
	 * Saves a medicine or any changes to the appropriate repo. 
	 * @param medicine - the medicine object to be saved. 
	 */
	void saveMedicine(Medicine medicine);
}