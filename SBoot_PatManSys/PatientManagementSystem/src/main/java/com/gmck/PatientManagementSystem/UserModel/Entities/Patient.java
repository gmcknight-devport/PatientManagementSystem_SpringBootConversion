package com.gmck.PatientManagementSystem.UserModel.Entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * Patient entity class extending the abstract User base class.
 * Corresponds to a database table and contains all information about patient users
 * with each variable having validation annotations. 
 * @author Glenn McKnight
 *
 */
@Entity
@Validated
@Table(name = "PATIENT")
public class Patient extends User {
		
	@Min(1)
	@Max(120)
	@Column(name = "AGE")
	private int age;
	
	@Embedded
	@JoinColumn(name = "USER_ID")
	Address address;
	
	@NotNull
	@Column(name = "GENDER")
	private char gender;
	
	/**
	 * Element collection of the embeddable object PatientNotes - stores
	 * multiple notes about each patient and managed by this class. 
	 */
	@ElementCollection
	@CollectionTable(name = "PATIENT_NOTES", joinColumns = @JoinColumn(name = "USER_ID"))
	@Column(name = "NOTES")
	private List<String> notes;
	
	/**
	 * Secondary table PatientPrescription is joined with a one to many - stores
	 * all patient prescriptions and is managed by this class. 
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "PATIENT_ID", referencedColumnName = "USER_ID", updatable = false, insertable = true)
	private List<PatientPrescription> prescriptions;
	    		
	Patient() {}
	
	public Patient(String userId, char[] userPassword, String title, String forename, String surname, int age, Address address, char gender, 
			ArrayList<String> notes, ArrayList<PatientPrescription> prescriptions) {
		super(userId, userPassword, title, forename, surname);
		this.age = age;
		this.address = address;
		this.gender = gender;
		this.notes = notes;		
		this.prescriptions = prescriptions;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public char getGender() {
		return gender;
	}

	/**
	 * Sets gender of the patient but enforces validation rules ensuring
	 * value confirms to required options. 
	 * @param gender - the gender to be set.
	 * @throws IllegalArgumentException - thrown if parameter is not an allowed value.
	 */
	public void setGender(char gender) throws IllegalArgumentException {
		final List<Character> genders = Arrays.asList('M', 'F', 'O');		
		
		if(genders.contains(gender)) {
			this.gender = gender;
		}else {
			this.gender = genders.get(0);
		}
	}

	public List<String> getNotes() {
		return notes;
	}

	void setNotes(List<String> notes) {
		this.notes = notes;
	}

	public void addNotes(String notes) {
		this.notes.add(notes);
	}
			
	public List<PatientPrescription> getPrescriptions() {
		return prescriptions;
	}

	void setPrescriptions(List<PatientPrescription> prescriptions) {
		this.prescriptions = prescriptions;
	}
	
	public void addPrescription(PatientPrescription prescription) {
		this.prescriptions.add(prescription);
	}
}
