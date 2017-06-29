package com.aavn.dashboardfakedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DashboardFakedataApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardFakedataApplication.class, args);
	}
}
