package com.gmck.PatientManagementSystem.Appointment;

import java.time.LocalTime;
import java.util.List;

/**
 * Interface class to enforce appointment properties configuration is implemented 
 * properly and includes all required getters and setters. 
 * @author Glenn McKnight
 *
 */
public interface IAppointmentProperties {

	LocalTime getOpeningTime();
	void setOpeningTime(LocalTime startTime);
	LocalTime getClosingTime();
	void setClosingTime(LocalTime endTime);
	List<String> getDaysOpen();
	void setDaysOpen(List<String> daysOpen);

}