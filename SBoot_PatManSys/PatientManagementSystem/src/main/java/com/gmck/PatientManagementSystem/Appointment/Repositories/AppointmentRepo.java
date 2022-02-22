package com.gmck.PatientManagementSystem.Appointment.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gmck.PatientManagementSystem.Appointment.Appointment;

/**
 * Repository for Appointment entity which extends JPA Repository class and adds 
 * additional methods to return more relevant and useful information
 * for operations to be applied to. 
 * @author Glenn McKnight
 *
 */
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
		
	@Query(value = "SELECT a FROM Appointment a WHERE a.doctorId = ?1 AND a.appointmentDate >= ?2 AND a.approved = ?3")
	List<Appointment> getDoctorAppointments(String doctorId, LocalDate date, boolean approved, Sort sort);
		
	@Query(value = "SELECT a FROM Appointment a WHERE a.patientId = ?1 AND a.appointmentDate >= ?2 AND a.approved = ?3")
	List<Appointment> getPatientAppointments(String patientId, LocalDate date, boolean approved, Sort sort);
	
	@Query(value = "SELECT a FROM Appointment a WHERE a.patientId = ?1 AND a.appointmentDate < ?2 AND a.approved = ?3")
	List<Appointment> getPreviousPatientAppointments(String patientId, LocalDate date, boolean approved, Sort sort);
			
	List<Appointment> findByApproved(boolean approved);
	
	//------------------------------------------------------------------------------------
	//Used to check for availability
	
	List<Appointment> findByDoctorIdAndAppointmentDateAndApproved(String doctorId, LocalDate appointmentDate, boolean approved);
	
	List<Appointment> findByPatientIdAndAppointmentDateAndApproved(String patientId, LocalDate appointmentDate, boolean approved);
}
