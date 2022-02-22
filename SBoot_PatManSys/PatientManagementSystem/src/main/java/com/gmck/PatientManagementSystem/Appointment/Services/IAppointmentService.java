package com.gmck.PatientManagementSystem.Appointment.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import com.gmck.PatientManagementSystem.Appointment.Appointment;

/**
 * Interface for AppointmentService to enforce the implementation of specified methods to ensure proper 
 * working of an appointment service class - allows operations to be carried out on
 * data from repository. 
 * @author Glenn McKnight
 *
 */
public interface IAppointmentService {

	//Numerous lists for different types of appointments to be returned
	List<Appointment> getPatientAppointments(String userId);
	List<Appointment> getDoctorAppointments(String userId);
	List<Appointment> getRequestedAppointments();
	List<Appointment> getRequestedAppointments(String patientId);
	List<Appointment> getPreviousAppointments(String patientId);

	/**
	 * Create an appointment from the parameters.
	 * @param patientId - The ID for the patient requiring the appointment.
	 * @param doctorId - The ID for the doctor.
	 * @param startTime - Time appointment begins.
	 * @param endTime - Time appointment ends.
	 * @param appointmentDate - Date appointment is on. 
	 * @param approved - Whether or not appointment has been approved. 
	 */
	void createAppointment(String patientId, String doctorId, LocalTime startTime, LocalTime endTime,
			LocalDate appointmentDate, boolean approved);

	/**
	 * Request an appointment from the parameters.
	 * @param patientId - The ID for the patient requiring the appointment.
	 * @param doctorId - Requested doctor ID.
	 * @param startTime - Requested time for appointment to begin.
	 * @param endTime - Requested time for appointment to end.
	 * @param appointmentDate - Requested appointment date. 
	 */
	void requestAppointment(String patientId, String doctorId, LocalTime startTime, LocalTime endTime,
			LocalDate appointmentDate);

	/**
	 * Approve an appointment specified by parameter by setting boolean to true. 
	 * @param id - the ID number of the appointment. 
	 */
	void approveAppointment(Long id);

	/**
	 * Decline an appointment specified by the id parameter removing it from repository, 
	 * and send a message to the user who requested the appointment.
	 * @param id - The ID of the appointment to be declined.
	 * @param senderId - ID of the person sending the message.
	 * @param senderName - Name of the person sending the message.
	 * @param message - The message ti be sent. 
	 */
	void declineAppointment(Long id, String senderId, String senderName, String message);

	/**
	 * Delete an appointment specified by the parameter - 
	 * check if it exists and remove it from the repository.
	 * @param id - The ID of the appointment.
	 */
	void deleteAppointment(Long id);
	
	/**
	 * Get a list of possible appointment times by checking IAppointmentConfig implementation. 
	 * @return - list of type Appointment. 
	 */
	List<Set<LocalTime>> getPossibleTimes();

	/**
	 * Save the appointment parameter to the repository.
	 * @param appointment - to be saved. 
	 */
	void saveAppointment(Appointment appointment);

}