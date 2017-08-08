package com.example.resources;

import java.io.IOException;

import javax.inject.Inject;

import org.atmosphere.config.service.WebSocketHandlerService;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.util.SimpleBroadcaster;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.json.pojo.Message;
import com.example.service.RabbitMQConnectionService;
import com.example.service.RabbitMQConnectionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

@WebSocketHandlerService(path = "/user", broadcaster = SimpleBroadcaster.class)
public class UserResource extends WebSocketHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WebSocketHandlerAdapter.class);

	private final ObjectMapper mapper = new ObjectMapper();
	
	private String EXCHANGE_NAME = "myExchange";

	private String ROUTING_KEY = "testRoute";

	private  Connection conn;

	private  Channel channel;

	private RabbitMQConnectionService connectionService = new RabbitMQConnectionServiceImpl();
	
	@Inject
	 BroadcasterFactory factory;
	
	
	public UserResource() {
		super();
		
		
	}

	@Override
	public void onOpen(WebSocket webSocket) throws IOException {
		Broadcaster broadcaster =  factory.lookup("/user");
		factory.add(broadcaster, "simple");
		webSocket.resource().setBroadcaster(factory.lookup("/user"));
		
		publishRabbitMQMessage("Connected");
	}

	private void publishRabbitMQMessage(String message) {
		Message message1 = new Message();
		message1.setContent(message);
		message1.setType("Message Type");
		conn = connectionService.getConnection();
		try {
			String messageJson = mapper.writeValueAsString(message1);
			channel = conn.createChannel();
			byte[] messageBodyBytes = messageJson.getBytes();
			System.out.println("Message to send");
			channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
					MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);

		} catch (IOException e) {
			LOGGER.error("Exception : ", e.getMessage());
		}
	}

	public void onTextMessage(WebSocket webSocket, String message)
			throws IOException {
		webSocket.broadcast(message);
	}

	@Override
	public void onClose(WebSocket webSocket) {
		publishRabbitMQMessage("Disconnected");
	}
}
