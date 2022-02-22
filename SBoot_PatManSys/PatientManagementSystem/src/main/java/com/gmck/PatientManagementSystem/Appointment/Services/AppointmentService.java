package com.gmck.PatientManagementSystem.Appointment.Services;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gmck.PatientManagementSystem.Appointment.Appointment;
import com.gmck.PatientManagementSystem.Appointment.IAppointmentProperties;
import com.gmck.PatientManagementSystem.Appointment.Repositories.AppointmentRepo;
import com.gmck.PatientManagementSystem.ErrorUpdate.ErrorUpdate;
import com.gmck.PatientManagementSystem.Messaging.Services.PatientMessageService;

/**
 * Appointment Service implementation of IAppointmentService interface. 
 * Performs operations on Appointment Entities and interacts with the repository 
 * storing them. Includes private internal methods to assist interface functions. 
 * @author Glenn McKnight
 *
 */
@Service
public class AppointmentService implements IAppointmentService {
	
	private Sort sort;	
	private IAppointmentProperties appointConfig;
	private AppointmentRepo appointmentRepo;
	private PatientMessageService patMessageService;
	
	/**
	 * Autowired constructor to increase ease of testing and reduce dependencies 
	 * from Autowiring individual variables. 
	 * @param appointConfig - IAppointmentConfig to be assigned. 
	 * @param appointmentRepo - Appointment Repository to be assigned. 
	 * @param patMessageService - PatientMessageService to be assigned. 
	 */
	@Autowired
	public AppointmentService(IAppointmentProperties appointConfig, AppointmentRepo appointmentRepo, PatientMessageService patMessageService) {
		this.appointConfig = appointConfig;
		this.appointmentRepo = appointmentRepo;
		this.patMessageService = patMessageService;
	}
	
	/**
	 * Return list of appointment for a patient from the specified User ID 
	 * parameter. Queries appropriate repo method. 
	 * @param userId - patient ID to return appointments for. 
	 */
	@Override
	public List<Appointment> getPatientAppointments(String userId){
		List<Appointment> appointList = new ArrayList<>();
		
		sort = Sort.by(Sort.Order.desc("appointmentDate"));
		appointList = appointmentRepo.getPatientAppointments(userId, LocalDate.now(), true, sort);
		
		return appointList;
	}
	
	/**
	 * Return list of appointment for a doctor from the specified User ID 
	 * parameter. Queries appropriate repo method. 
	 * @param userId - doctor ID to return appointments for. 
	 */
	@Override
	public List<Appointment> getDoctorAppointments(String userId){
		List<Appointment> appointList = new ArrayList<>();
		
		sort = Sort.by(Sort.Order.desc("appointmentDate"));
		appointList = appointmentRepo.getDoctorAppointments(userId, LocalDate.now(), true, sort);
		
		return appointList;
	}
	
	/**
	 * Return list of of requested appointments from the repository using the appropriate repo method.
	 */
	@Override
	public List<Appointment> getRequestedAppointments(){
		List<Appointment> appointList = new ArrayList<>();
		
		sort = Sort.by(Sort.Order.desc("appointmentDate"));
		appointList = appointmentRepo.findByApproved(false);
		
		return appointList;
	}
	
	/**
	 * Return list of of requested appointments from the repository using 
	 * the appropriate repo method for a patient specified by parameter.
	 * @param patientId - ID of specified patient to get appointments for. 
	 */
	@Override
	public List<Appointment> getRequestedAppointments(String patientId) {
		List<Appointment> appointList = new ArrayList<>();
		
		sort = Sort.by(Sort.Order.desc("appointmentDate"));
		appointList = appointmentRepo.getPatientAppointments(patientId, LocalDate.now(), false, sort);
		
		return appointList;
	}
	
	/**
	 * Return list of of previous appointments before the current date from the repository using 
	 * the appropriate repo method for a patient specified by parameter.
	 * @param patientId - ID of specified patient to get appointments for. 
	 */
	@Override
	public List<Appointment> getPreviousAppointments(String patientId){
		sort = Sort.by(Sort.Order.desc("appointmentDate"));
		List<Appointment> previousAppointments = appointmentRepo.getPreviousPatientAppointments(patientId, LocalDate.now(), true, sort);
		
		return previousAppointments;
	}
		
	@Override
	public void createAppointment(String patientId, String doctorId, LocalTime startTime, 
			LocalTime endTime, LocalDate appointmentDate, boolean approved) {
		
		if(validateAppointment(startTime, endTime, appointmentDate) == true 
				&& checkFree(patientId, doctorId, appointmentDate, startTime, endTime) == true) {		
			
			addAppointment(patientId, doctorId, startTime, endTime, appointmentDate, true);
		}else {
			ErrorUpdate.getInstance().updateObserver("Appointment not valid - check times are within working hours");
		}
	}
	
	@Override
	public void requestAppointment(String patientId, String doctorId, LocalTime startTime, LocalTime endTime,
			LocalDate appointmentDate) {
		
		if(validateAppointment(startTime, endTime, appointmentDate) == true 
				&& checkFree(patientId, doctorId, appointmentDate, startTime, endTime) == true) {	
			
			addAppointment(patientId, doctorId, startTime, endTime, appointmentDate, false);
		}
	}
	
	/**
	 * Internal method to be called by RequestAppointment and CreateAppointment 
	 * methods to create a new Appointment from the parameters and call saveAppointment().
	 * @param patientId - The ID for the patient requiring the appointment.
	 * @param doctorId - The ID for the doctor.
	 * @param startTime - Time appointment begins.
	 * @param endTime - Time appointment ends.
	 * @param appointmentDate - Date appointment is on. 
	 * @param approved - Whether or not appointment has been approved. 
	 */
	private void addAppointment(String patientId, String doctorId, LocalTime startTime, LocalTime endTime,
			LocalDate appointmentDate, boolean approved) {
		Appointment appointment = new Appointment();
		appointment.setPatientId(patientId);
		appointment.setDoctorId(doctorId);
		appointment.setStartTime(startTime);
		appointment.setEndTime(endTime);
		appointment.setAppointmentDate(appointmentDate);
		appointment.setApproved(approved);
		
		saveAppointment(appointment);
	}
	
	@Override
	public void approveAppointment(Long id) {
		Appointment appointment = appointmentRepo.getById(id);
		appointment.setApproved(true);
		
		saveAppointment(appointment);
	}
	
	@Override
	public void declineAppointment(Long id, String senderId, String senderName, String message) {
		LocalDateTime currDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);			
		Appointment appointment = appointmentRepo.getById(id);
		
		message += ", " + appointment.getAppointmentDate() + ", " + appointment.getStartTime() 
			+ "-" + appointment.getEndTime() + " with: " + appointment.getDoctorId() + ".";		
		
		patMessageService.createMessage(appointment.getPatientId(), senderId, senderName, currDate, message);		
		appointmentRepo.delete(appointment);
	}
	
	@Override
	public void deleteAppointment(Long id) {
		try {
			appointmentRepo.deleteById(id);
			appointmentRepo.flush();
			
		}catch(IllegalArgumentException ex) {
			ErrorUpdate.getInstance().updateObserver("Exception - couldn't find appointment to delete");
		}
	}
	
	/**
	 * Ensure an appointment is valid - parameters fall between allowable dates and times and 
	 * take place after today. 
	 * @param appointStart - Time appointment begins.
	 * @param appointEnd - Time appointment ends.
	 * @param appointDate - Date appointment is on. 
	 * @return
	 */
	private boolean validateAppointment(LocalTime appointStart, LocalTime appointEnd, LocalDate appointDate) {		
		boolean isValid = false;
				
		if(appointConfig.getDaysOpen().contains(appointDate.getDayOfWeek().toString()) &&
				(appointDate.isAfter(LocalDate.now()) || appointDate.isEqual(LocalDate.now()))) {
			
			if((appointStart.equals(appointConfig.getOpeningTime()) || appointStart.isAfter(appointConfig.getOpeningTime()))
					&& appointEnd.equals(appointConfig.getClosingTime()) || appointEnd.isBefore(appointConfig.getClosingTime())){
				isValid = true;
			}
		}
		return isValid;
	}
	
	/**
	 * Gets lists of patient and doctor appointments from repositories for specified user IDs. Calls 
	 * checkTimes method, passing a list to check, and report via Error observer if user
	 * isn't available.
	 * @param patientId - The ID for the patient requiring the appointment.
	 * @param doctorId - The ID for the doctor of the appointment.
	 * @param requestedDate - Date appointment is on.
	 * @param startTime - Requested time for appointment to begin.
	 * @param endTime - Requested time for appointment to end.
	 * @return boolean - true if users are free. 
	 */
	private boolean checkFree(String patientId, String doctorId, LocalDate requestedDate, LocalTime requestedStart, LocalTime requestedEnd) {
		
		List<Appointment> patAppoints = appointmentRepo.findByPatientIdAndAppointmentDateAndApproved(patientId, requestedDate, true);
		List<Appointment> docAppoints = appointmentRepo.findByDoctorIdAndAppointmentDateAndApproved(doctorId, requestedDate, true);
		
		if(!checkTimes(patAppoints, requestedStart, requestedEnd)) {
			ErrorUpdate.getInstance().updateObserver("Patient isn't free at this time");
			return false;
		}else if(!checkTimes(docAppoints, requestedStart, requestedEnd)) {
			ErrorUpdate.getInstance().updateObserver("Doctor isn't free at this time");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if appointment times are already taken. 
	 * Checks the requested start and end times don't fall within those of any other 
	 * appointments in the appointList parameter, and that it is within appintmentConfig hours.
	 * @param appointList - list of appointments to check start and end times against. 
	 * @param requestedStart - Requested time for appointment to begin.
	 * @param requestedEnd - Requested time for appointment to end.
	 * @return boolean - true if times are valid and appointment can be created. 
	 */
	private boolean checkTimes(List<Appointment> appointList, LocalTime requestedStart, LocalTime requestedEnd) {
		boolean isValid = true;
		
		for(Appointment a : appointList) {
			
			if(a.getStartTime() == requestedStart || a.getEndTime() == a.getEndTime()) {
				ErrorUpdate.getInstance().updateObserver("Requested appointment is outside working hours");
				isValid = false;
			}else if(a.getStartTime().isAfter(requestedStart) && a.getStartTime().isBefore(requestedEnd)) {
				ErrorUpdate.getInstance().updateObserver("Requested appointment is outside working hours");
				isValid = false;
			}else if(a.getEndTime().isAfter(requestedStart) && a.getEndTime().isBefore(requestedEnd)) {
				ErrorUpdate.getInstance().updateObserver("Requested appointment is outside working hours");
				isValid = false;
			}			
		}
		
		return isValid;
	}
	
	@Override
	public List<Set<LocalTime>> getPossibleTimes() {
		List<Set<LocalTime>> appointTimes = new ArrayList<>();
		LocalTime startTime = appointConfig.getOpeningTime();
		LocalTime endTime = startTime.plusMinutes(30);
		Set<LocalTime> slot;
		
		while(!endTime.equals(appointConfig.getClosingTime())) {
			slot = new TreeSet<>();
			slot.add(startTime);
			slot.add(endTime);
			appointTimes.add(slot);
			
			startTime = startTime.plusMinutes(30);
			endTime = endTime.plusMinutes(30);
		}
				
		return appointTimes;
	}
	
	@Override
	public void saveAppointment(Appointment appointment) {
		try {
			appointmentRepo.save(appointment);
		}catch(ConstraintViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Appointment does not conform to validation rules - ensure there aren't "
					+ "null or incorrectly formatted values");
		}catch(DataIntegrityViolationException e) {
			ErrorUpdate.getInstance().updateObserver("Referenced foreign key field doesn't exist");
		}
	}	
}
