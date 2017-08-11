package com.example.resources;

import java.io.IOException;




import org.atmosphere.config.service.WebSocketHandlerService;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketHandlerAdapter;

import com.example.broadcast.filter.PerRequestBroadcastFilterImpl;
import com.example.broadcaster.AdminListenerBroadcaster;
import com.google.inject.Inject;

@WebSocketHandlerService(path = "/admin", broadcaster = AdminListenerBroadcaster.class)
public class AdminResource extends WebSocketHandlerAdapter {

	@Inject
	AdminListenerBroadcaster broadcastera;
	
	@Inject
	BroadcasterFactory factory;
	
	@Inject
	public AdminResource( BroadcasterFactory factory, AdminListenerBroadcaster broadcastera) {

		this.factory = factory;
		this.broadcastera = broadcastera;
	}

	@Override
	public void onOpen(WebSocket webSocket) throws IOException {
		Broadcaster broadcastera = factory.lookup("/admin");
		factory.add(broadcastera, "simple");
		PerRequestBroadcastFilter broadCastFilter = new PerRequestBroadcastFilterImpl();
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
