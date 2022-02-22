package com.gmck.PatientManagementSystem.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Interface class to enforce an appointment implementation class includes the 
 * required setters and getters for proper operation and avoidance of errors. 
 * @author Glenn McKnight
 *
 */
public interface IAppointment {

	Long getId();
	String getPatientId();
	void setPatientId(String patientId);
	String getDoctorId();
	void setDoctorId(String doctorId);
	LocalTime getStartTime();
	void setStartTime(LocalTime startTime);
	LocalTime getEndTime();
	void setEndTime(LocalTime endTime);
	LocalDate getAppointmentDate();
	void setAppointmentDate(LocalDate appointmentDate);
	boolean isApproved();
	void setApproved(boolean approved);
}