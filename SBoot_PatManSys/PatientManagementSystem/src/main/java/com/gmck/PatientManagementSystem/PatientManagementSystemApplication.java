package com.gmck.PatientManagementSystem;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main method and entry point for the application running using the Spring
 * Application Builder without headless mode turned off. 
 * @author Glenn McKnight
 *
 */
@SpringBootApplication(scanBasePackages = { "com.gmck" })
public class PatientManagementSystemApplication { //implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(PatientManagementSystemApplication.class);
		builder.headless(false);
		
		@SuppressWarnings("unused")
		ConfigurableApplicationContext context = builder.run(args);		
	}
}
