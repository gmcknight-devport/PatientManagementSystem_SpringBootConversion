package com.gmck.PatientManagementSystem.UserModel;

/**
 * Stores Enums for each type of user present in the system. Allows
 * for operations to be carried out on a consistent set of users at
 * runtime. 
 * @author Glenn McKnight
 *
 */
public enum UserType {

	P, //Patient	
	D, //Doctor
	A, //Administrator
	S, //Secretary
	T, //Temporary
	L  //LoggedOutUser
}
