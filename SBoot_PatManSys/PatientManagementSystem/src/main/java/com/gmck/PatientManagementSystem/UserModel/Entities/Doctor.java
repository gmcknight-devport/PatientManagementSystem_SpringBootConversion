package com.gmck.PatientManagementSystem.UserModel.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

/**
 * Doctor entity class extending the abstract User base class.
 * Corresponds to a database table and contains all information about doctor users
 * with each variable having validation annotations. 
 * @author Glenn McKnight
 *
 */
@Entity
public class Doctor extends User {
	
	@Embedded
	@JoinColumn(name = "USER_ID")
	Address surgeryAddress;
		
	/**
	 * Element collection of the embeddable object DoctorRatings - stores
	 * multiple ratings about each doctor and is managed by this class. 
	 */
	@ElementCollection
	@CollectionTable(name = "DOC_RATING", joinColumns = @JoinColumn(name = "DOCTOR_ID", referencedColumnName = "USER_ID"))
	@Column(name = "RATING")
	private List<Double> ratings;
	
	Doctor() {}

	public Doctor(String userId, char[] userPassword, String title, String forename, String surname, Address surgeryAddress, ArrayList<Double> ratings) {
		super(userId, userPassword, title, forename, surname);
		this.surgeryAddress = surgeryAddress;
		this.ratings = ratings;
	}

	public Address getSurgeryAddress() {
		return surgeryAddress;
	}

	public void setSurgeryAddress(Address surgeryAddress) {
		this.surgeryAddress = surgeryAddress;
	}

	public List<Double> getRatings() {
		return ratings;
	}

	void setRatings(List<Double> ratings) {
		this.ratings = ratings;
	}
	
	public void addRating(int rating) {
		this.ratings.add((double) rating);
	}
}
