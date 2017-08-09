package com.connection.app;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.ServletRegistration;
import javax.ws.rs.core.MediaType;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;

import com.example.module.ConnectionModule;
import com.hubspot.dropwizard.guice.GuiceBundle;

public class WebsocketConnectionApp extends Application<WebsocketConnectionConfiguration> {
	
	GuiceBundle<WebsocketConnectionConfiguration> guiceBundle;
    public static void main(String[] args) throws Exception {
        new WebsocketConnectionApp().run(args);

    }

    @Override
    public void initialize(Bootstrap<WebsocketConnectionConfiguration> bootstrap) {
        
    	guiceBundle = GuiceBundle.<WebsocketConnectionConfiguration>newBuilder()
        		.addModule(new ConnectionModule())
        	      .enableAutoConfig(getClass().getPackage().getName())
        	      .setConfigClass(WebsocketConnectionConfiguration.class)
        	      .build();

        	    bootstrap.addBundle(guiceBundle);
    }

	@Override
	public void run(WebsocketConnectionConfiguration configuration,
			Environment environment) throws Exception {
		 AtmosphereServlet atmosphereServlet = new AtmosphereServlet();
	        atmosphereServlet.framework().addInitParameter("com.sun.jersey.config.property.packages",
	                "com.example.resources");
	        
	        atmosphereServlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
	        atmosphereServlet.framework().addInitParameter("org.atmosphere.websocket.messageContentType",
	                MediaType.APPLICATION_JSON);
	        atmosphereServlet.framework().addInitParameter("org.atmosphere.cpr.AtmosphereFramework.analytics", "false");
	        atmosphereServlet.framework().addInitParameter("org.atmosphere.websocket.maxTextMessageSize", "999999");
	        atmosphereServlet.framework().addInitParameter("org.atmosphere.websocket.suppressJSR356", "true");

	        ServletRegistration.Dynamic servletHolder = environment.servlets().addServlet("AtmosphereServlet",
	                atmosphereServlet);
	        servletHolder.addMapping("/user");
	        
	        
	        AtmosphereServlet atmosphereServlet1 = new AtmosphereServlet();
	        atmosphereServlet1.framework().addInitParameter("com.sun.jersey.config.property.packages",
	                "com.example.resources");
	        
	        atmosphereServlet1.framework().addInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
	        atmosphereServlet1.framework().addInitParameter("org.atmosphere.websocket.messageContentType",
	                MediaType.APPLICATION_JSON);
	        atmosphereServlet1.framework().addInitParameter("org.atmosphere.cpr.AtmosphereFramework.analytics", "false");
	        atmosphereServlet1.framework().addInitParameter("org.atmosphere.websocket.maxTextMessageSize", "999999");
	        atmosphereServlet1.framework().addInitParameter("org.atmosphere.websocket.suppressJSR356", "true");
	        
	        ServletRegistration.Dynamic servletHolder1 = environment.servlets().addServlet("AtmosphereServlet1",
	        		atmosphereServlet1);
	        
	        servletHolder1.addMapping("/admin");
		
	}
}
