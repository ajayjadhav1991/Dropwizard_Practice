package com.example.module;

import com.example.service.RabbitMQConnectionService;
import com.example.service.RabbitMQConnectionServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;



public class ConnectionModule extends  AbstractModule {


	@Override
	protected void configure() {
		
		 bind(RabbitMQConnectionService.class).to(RabbitMQConnectionServiceImpl.class).in(Singleton.class);
	}

}
