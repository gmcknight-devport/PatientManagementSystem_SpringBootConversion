package com.gmck.PatientManagementSystem.UserModel.Entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Embeddable class to store ratings for the Doctor entity class. 
 * @author Glenn McKnight
 *
 */
@Embeddable
public class DoctorRatings {

	@Id
	@Pattern(regexp = "^[D][0-9][0-9][0-9][0-9]$")
	@Column(name = "DOCTOR_ID")
	private String userId;
	
	@Min(0)
	@Max(5)
	@NotBlank
	@Column(name = "RATING")
	private double rating;
}
