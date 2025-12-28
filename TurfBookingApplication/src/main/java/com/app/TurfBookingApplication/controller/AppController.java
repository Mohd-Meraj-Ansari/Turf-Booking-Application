package com.app.TurfBookingApplication.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
	private static final Logger logger=Logger.getLogger(AppController.class);

	static {
	    logger.error("==== LOG4J FILE TEST ====");
	}
	@GetMapping("/test")
	public String test() {
		logger.info("request recieved in controller for test endpoint");
		return "test endpoint";
	}
}
