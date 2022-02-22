package com.gmck.PatientManagementSystem.UserModel.Entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Embeddable class to store addresses for entity classes. 
 * @author Glenn McKnight
 *
 */
@Embeddable
public class Address {
	
	@Id
	@Pattern(regexp = "^[ADPST][0-9][0-9][0-9][0-9]$")
	@Column(name = "USER_ID")
	private String userId;
	
	@NotBlank
	@Column(name = "ADDRESS_LINE1")
	private String addressLine1;
	
	@Column(name = "ADDRESS_LINE2")
	private String addressLine2;
	
	@NotBlank
	@Column(name = "CITY")
	private String city;
	
	@Pattern(regexp = "^[A-Z]{1,2}[0-9][A-Z0-9]? ?[0-9][A-Z]{2}$")
	@Column(name = "POSTCODE")
	private String postcode;

	Address() {}
	
	public Address(String userId, String addressLine1, String addressLine2, String city, String postcode) {
		this.userId = userId;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.postcode = postcode;
	}

	public String getUserId() {
		return userId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public String getCity() {
		return city;
	}

	public String getPostcode() {
		return postcode;
	}		
}
