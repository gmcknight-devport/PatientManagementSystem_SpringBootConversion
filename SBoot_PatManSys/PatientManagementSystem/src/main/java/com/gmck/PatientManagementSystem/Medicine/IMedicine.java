package com.gmck.PatientManagementSystem.Medicine;

/**
 * Interface to enforce the implementation of specified methods to ensure proper 
 * working of a medicine entity class.
 * @author Glenn McKnight
 *
 */
public interface IMedicine {

	String getMedName();
	void setMedName(String medName);
	String getMedDosage();
	void setMedDosage(String medDosage);
	String getCommonUses();
	void setCommonUses(String commonUses);
	int getQuantity();
	void setQuantity(int quantity);
}