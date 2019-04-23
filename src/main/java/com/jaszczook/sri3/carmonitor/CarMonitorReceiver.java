package com.jaszczook.sri3.carmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CarMonitorReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(CarMonitorReceiver.class);

	private final MessageRouter messageRouter;

	@Autowired
	public CarMonitorReceiver(MessageRouter messageRouter) {
		this.messageRouter = messageRouter;
	}

	@JmsListener(destination = "carData.t")
	public void receive(CarData carData) {
		LOGGER.info("received car data = {}", carData);
		messageRouter.route(carData);
	}
}
