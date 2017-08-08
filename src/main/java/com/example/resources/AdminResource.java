package com.example.resources;

import java.io.IOException;

import javax.inject.Inject;

import org.atmosphere.config.service.WebSocketHandlerService;
import org.atmosphere.cpr.AtmosphereResourceSessionFactory;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketHandlerAdapter;

import com.example.broadcast.filter.PerRequestBroadcastFilterImpl;
import com.example.broadcaster.AdminListenerBroadcaster;

@WebSocketHandlerService(path = "/admin", broadcaster = AdminListenerBroadcaster.class)
public class AdminResource extends WebSocketHandlerAdapter {

	
	AdminListenerBroadcaster broadcastera = new AdminListenerBroadcaster();
	
	@Inject
	BroadcasterFactory factory;
	
	@Inject
	AtmosphereResourceSessionFactory atmResourceSessionfactory;

	
	public AdminResource() {
		super();
		
	}

	@Override
	public void onOpen(WebSocket webSocket) throws IOException {
		Broadcaster broadcastera = factory.lookup("/admin");
		factory.add(broadcastera, "simple");
		PerRequestBroadcastFilter broadCastFilter = new PerRequestBroadcastFilterImpl();
		//Broadcaster broadcaster = factory.lookup("/admin");
		broadcastera.getBroadcasterConfig().addFilter(broadCastFilter);
		webSocket.resource().setBroadcaster(factory.lookup("/admin"));
	}

	public void onTextMessage(WebSocket webSocket, String message)
			throws IOException {
		  webSocket.resource().getAtmosphereConfig().sessionFactory().getSession(webSocket.resource()).setAttribute("uppercase", message);

	}

	public void onComplete(Broadcaster b) {
		System.out.println("Completed");
	}
}
