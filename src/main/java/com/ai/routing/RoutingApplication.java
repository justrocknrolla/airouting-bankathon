package com.ai.routing;

import com.ai.routing.services.HistoryImporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ai.routing")
public class RoutingApplication {

	private final HistoryImporterService historyImporterService;

	@Autowired
	public RoutingApplication(HistoryImporterService historyImporterService) {
		this.historyImporterService = historyImporterService;
		historyImporterService.importData(RoutingApplication.class.getResourceAsStream("/initial_data.txt"));
	}

	public static void main(String[] args) {
		SpringApplication.run(RoutingApplication.class, args);
	}
}
