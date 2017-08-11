package com.example.module;



import org.atmosphere.guice.AtmosphereGuiceServlet;

import com.example.broadcaster.AdminListenerBroadcaster;
import com.example.service.RabbitMQConnectionService;
import com.example.service.RabbitMQConnectionServiceImpl;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;




public class ConnectionModule  implements  Module {


	/*@Override
	protected void configure() {
		RabbitMQConnectionService rabbitMQConnectionService = new RabbitMQConnectionServiceImpl();
		
		 bind(RabbitMQConnectionService.class).to(RabbitMQConnectionServiceImpl.class).in(Singleton.class);
	}
*/
	@Override
	public void configure(Binder binder) {
		binder.bind(RabbitMQConnectionService.class).to(RabbitMQConnectionServiceImpl.class).in(Singleton.class);
		binder.bind(AdminListenerBroadcaster.class).in(Singleton.class);
		binder.bind(AtmosphereGuiceServlet.class);
	}


}
