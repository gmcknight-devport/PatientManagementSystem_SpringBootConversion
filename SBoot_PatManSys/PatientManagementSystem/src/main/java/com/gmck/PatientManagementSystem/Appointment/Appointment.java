package com.gmck.PatientManagementSystem.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Entity class for Appointments, corresponds to a database table. 
 * Contains all information about each appointment with each variable having validation annotations.
 * @author Glenn McKnight
 *
 */
@Entity
public class Appointment implements IAppointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Long id;
	
	@Pattern(regexp = "^[P][0-9][0-9][0-9][0-9]$")
	@Column(name = "PATIENT_ID")
	private String patientId;
		
	@Pattern(regexp = "^[D][0-9][0-9][0-9][0-9]$")
	@Column(name = "DOCTOR_ID")
	private String doctorId;
	
	@NotNull
	@Column(name = "APPOINTMENT_START_TIME")
	private LocalTime startTime;
	
	@NotNull
	@Column(name = "APPOINTMENT_END_TIME")
	private LocalTime endTime;
	
	@NotNull
	@Column(name = "APPOINTMENT_DATE")
	private LocalDate appointmentDate;
	
	@NotNull
	@Column(name = "APPROVED")
	private boolean approved;

	public Appointment() {}
	
	protected Appointment(Long id, String patientId, String doctorId, LocalTime startTime, LocalTime endTime,
			LocalDate appointmentDate, boolean approved) {
		this.id = id;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.appointmentDate = appointmentDate;
		this.approved = approved;
	}

	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public String getPatientId() {
		return patientId;
	}

	@Override
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	@Override
	public String getDoctorId() {
		return doctorId;
	}

	@Override
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	@Override
	public LocalTime getStartTime() {
		return startTime.truncatedTo(ChronoUnit.MINUTES);
	}

	@Override
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime.truncatedTo(ChronoUnit.MINUTES);
	}

	@Override
	public LocalTime getEndTime() {
		return endTime.truncatedTo(ChronoUnit.MINUTES);
	}

	@Override
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime.truncatedTo(ChronoUnit.MINUTES);
	}

	@Override
	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	@Override
	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	@Override
	public boolean isApproved() {
		return approved;
	}

	@Override
	public void setApproved(boolean approved) {
		this.approved = approved;
	}	
}
