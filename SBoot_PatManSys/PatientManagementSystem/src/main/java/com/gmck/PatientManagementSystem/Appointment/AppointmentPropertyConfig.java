package com.gmck.PatientManagementSystem.Appointment;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for appointment properties - loads opening and closing time, and opening days
 * set in the application properties file.
 * @author Glenn McKnight
 *
 */
@Configuration
@ConfigurationProperties(prefix = "appointment")
public class AppointmentPropertyConfig implements IAppointmentProperties {

	private LocalTime openingTime;
	private LocalTime closingTime;
	private List<String> daysOpen = new ArrayList<String>();
	
	@Override
	public LocalTime getOpeningTime() {
		return openingTime;
	}

	@Override
	public void setOpeningTime(LocalTime startTime) {
		this.openingTime = startTime;
	}

	@Override
	public LocalTime getClosingTime() {
		return closingTime;
	}

	@Override
	public void setClosingTime(LocalTime endTime) {
		this.closingTime = endTime;
	}

	@Override
	public List<String> getDaysOpen() {
		return daysOpen;
	}

	@Override
	public void setDaysOpen(List<String> daysOpen) {
		this.daysOpen = daysOpen;
	}	
}
