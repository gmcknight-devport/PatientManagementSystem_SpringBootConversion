package com.gmck.PatientManagementSystem.Medicine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Concrete implementation of the IMedicine interface to create a medicine
 * entity class. Contains all information about medicine objects with each variable
 * having validation annotations. Has a corresponding repository.
 * @author Glenn McKnight
 *
 */
@Entity
public class Medicine implements IMedicine {
		
	@Id	
	@NotBlank
	@Column(name = "MEDICINE_NAME")
	private String medName;
	
	@NotBlank
	@Column(name = "MEDICINE_DOSAGE")
	private String medDosage;
	
	@NotBlank
	@Column(name = "COMMON_USES")
	private String commonUses;
	
	@Min(0)
	@Column(name = "QUANTITY")
	private int quantity;
	
	public Medicine() {}

	public Medicine(String medName, String medDosage, String commonUses, int quantity) {		
		this.medName = medName;
		this.medDosage = medDosage;
		this.commonUses = commonUses;
		this.quantity = quantity;
	}

	@Override
	public String getMedName() {
		return medName;
	}

	@Override
	public void setMedName(String medName) {
		this.medName = medName;
	}

	@Override
	public String getMedDosage() {
		return medDosage;
	}

	@Override
	public void setMedDosage(String medDosage) {
		this.medDosage = medDosage;
	}

	@Override
	public String getCommonUses() {
		return commonUses;
	}

	@Override
	public void setCommonUses(String commonUses) {
		this.commonUses = commonUses;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
