package com.gmck.PatientManagementSystem.UserModel.Entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Secondary table entity class containing all information about prescriptions
 * for a corresponding patient entity. 
 * @author Glenn McKnight
 *
 */
@Entity
@Table(name = "Prescriptions")
public class PatientPrescription {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Pattern(regexp = "^[P][0-9][0-9][0-9][0-9]$")
	@Column(name = "PATIENT_ID")
	private String patientId;
	
	@NotBlank
	@Column(name = "PRESCRIPTION")
	private String prescription;
	
	@Pattern(regexp = "^[D][0-9][0-9][0-9][0-9]$")
	@Column(name = "DOCTOR_ID")
	private String doctorId;
	
	@NotNull
	@Column(name = "PRESCRIPTION_DATE")
	private LocalDate prescriptionDate;
	
	PatientPrescription() {}

	public PatientPrescription(String patientId, String prescription, String doctorId, LocalDate prescriptionDate) {
		this.patientId = patientId;
		this.prescription = prescription;
		this.doctorId = doctorId;
		this.prescriptionDate = prescriptionDate;
	}

	public String getPatientId() {
		return patientId;
	}

	public String getPrescription() {
		return prescription;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public LocalDate getPrescriptionDate() {
		return prescriptionDate;
	}
}
