package com.example.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class RabbitMQConnectionServiceImpl implements RabbitMQConnectionService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RabbitMQConnectionServiceImpl.class);

	private static final String LOCALHOST = "localhost";

	private static final String GUEST = "guest";

	private Connection connection;

	public RabbitMQConnectionServiceImpl() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(GUEST);
		factory.setPassword(GUEST);
		factory.setHost(LOCALHOST);// 127.0.0.1
		factory.setPort(5672);//
		try {
			connection = factory.newConnection();
		} catch (IOException e) {
			LOGGER.error("Exception : ", e.getMessage());
		}
	}

	@Override
	public Connection getConnection() {

		return connection;
	}
}
