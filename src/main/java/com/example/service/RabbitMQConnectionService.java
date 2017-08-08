package com.example.service;

import com.rabbitmq.client.Connection;


public interface RabbitMQConnectionService {

	Connection getConnection();

}
