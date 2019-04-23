package com.jaszczook.sri3.carmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageRouter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);

	private static final String CAR_QUEUE = "car.q";
	private static final String PIT_QUEUE = "pit.q";

	private final JmsTemplate jmsTemplate;

	@Autowired
	public MessageRouter(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void route(CarData carData) {

		boolean warnDriver = checkWhetherToWarnDriver(carData);
		boolean warnPit = checkWhetherToWarnPit(carData);

		if (warnDriver) {
			warnDriver(carData);
		}

		if (warnPit) {
			warnPit(carData);
		}
	}

	private boolean checkWhetherToWarnDriver(CarData carData) {
		double oilTemperature = carData.getOilTemperature();
		double tiresTemperature = carData.getTiresTemperature();
		double fuelLevel = carData.getFuelLevel();

		if (oilTemperature > 150) {
			return true;
		} else if (oilTemperature < 90) {
			return true;
		}

		if (tiresTemperature > 150) {
			return true;
		} else if (tiresTemperature < 90) {
			return true;
		}

		if (fuelLevel < 30) {
			return true;
		}

		return false;
	}

	private boolean checkWhetherToWarnPit(CarData carData) {
		double oilTemperature = carData.getOilTemperature();
		double tiresTemperature = carData.getTiresTemperature();
		double fuelLevel = carData.getFuelLevel();

		if (oilTemperature > 170) {
			return true;
		} else if (oilTemperature < 70) {
			return true;
		}

		if (tiresTemperature > 170) {
			return true;
		} else if (tiresTemperature < 70) {
			return true;
		}

		if (fuelLevel < 20) {
			return true;
		}

		return false;
	}

	private void warnDriver(CarData carData) {
		LOGGER.info("sending message to driver='{}'", carData);
		jmsTemplate.convertAndSend(CAR_QUEUE, carData);
	}

	private void warnPit(CarData carData) {
		LOGGER.info("sending message to pit='{}'", carData);
		jmsTemplate.convertAndSend(PIT_QUEUE, carData);
	}
}
