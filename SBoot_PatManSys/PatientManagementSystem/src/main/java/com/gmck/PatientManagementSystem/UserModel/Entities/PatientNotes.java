package com.gmck.PatientManagementSystem.UserModel.Entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Embeddable class to store notes for the patient entity. 
 * @author Glenn McKnight
 *
 */
@Embeddable
public class PatientNotes {

	@Id
	@Pattern(regexp = "^[P][0-9][0-9][0-9][0-9]$")
	@Column(name = "USER_ID")
	private String userId;
	
	@NotBlank
	@Column(name = "NOTES")
	private String notes;

	public String getNotes() {
		return notes;
	}
}
