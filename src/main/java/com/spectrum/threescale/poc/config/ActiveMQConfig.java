package com.spectrum.threescale.poc.config;

import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class ActiveMQConfig {

	@Value("${activemq.broker-url}")
	private String brokerUrl;

	@Value("${activemq.broker-username}")
	private String username;

	@Value("${activemq.broker-password}")
	private String password;

	@Bean
	public Queue queue() {
		return new ActiveMQQueue("poc-activemq-queue");
	}

	@Bean
	public ActiveMQConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerUrl);
		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);
		return connectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		return new JmsTemplate(connectionFactory());
	}
}
