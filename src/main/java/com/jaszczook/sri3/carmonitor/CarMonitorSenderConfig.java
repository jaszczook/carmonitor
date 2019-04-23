package com.jaszczook.sri3.carmonitor;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CarMonitorSenderConfig {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Bean
	public ActiveMQConnectionFactory senderActiveMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);

		return activeMQConnectionFactory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory(senderActiveMQConnectionFactory());
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory());
		jmsTemplate.setMessageConverter(senderJacksonJmsMessageConverter());

		return jmsTemplate;
	}

	@Bean
	public JmsTemplate jmsQueueTemplate() {
		return new JmsTemplate(cachingConnectionFactory());
	}

	@Bean
	public MessageConverter senderJacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

		Map<String, Class<?>> typeIdMappings = new HashMap<>();
		typeIdMappings.put("CAR_DATA_TYPE", CarData.class);

		converter.setTypeIdMappings(typeIdMappings);
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("CAR_DATA_TYPE");

		return converter;
	}
}
