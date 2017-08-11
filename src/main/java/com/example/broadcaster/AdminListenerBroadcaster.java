package com.example.broadcaster;

import java.io.IOException;

import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFuture;
import org.atmosphere.cpr.Deliver;
import org.atmosphere.util.SimpleBroadcaster;

import com.example.json.pojo.Message;
import com.example.service.RabbitMQConnectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class AdminListenerBroadcaster extends SimpleBroadcaster {

	private String queueName;
	
	private Channel channel;
	
	private String exchangeName;
	 
	private String consumerTag;
	
	private int count = 0;
	
	@Inject
	private RabbitMQConnectionService connectionService;
	
	private final ObjectMapper mapper = new ObjectMapper();
	 
	 
	  @Override
	public Broadcaster initialize(String id, AtmosphereConfig config) {
		super.initialize(id, config);
		init();

		return this;
	}
	  @Override
	    public Broadcaster initialize(String name, java.net.URI uri, AtmosphereConfig config) {
	        super.initialize(name, uri, config);
	        init();
	        return this;
	    }
	  public void init() {
	        exchangeName = "myExchange";
			try {
				Connection conn = connectionService.getConnection();
				channel = conn.createChannel();
				queueName = channel.queueDeclare().getQueue();
				channel.queueBind(queueName, exchangeName, "testRoute");

				DefaultConsumer queueConsumer = new DefaultConsumer(channel) {
					@Override
					public void handleDelivery(String consumerTag,
							Envelope envelope, AMQP.BasicProperties properties,
							byte[] body) throws IOException {

						if (!envelope.getRoutingKey().equalsIgnoreCase("testRoute"))
							return;

						String message = new String(body);
						
						Message messageNode = mapper.readValue(message, Message.class);
						if( messageNode.getContent().equalsIgnoreCase("Connected") ){
							count++;
						}
						else{
							count--;
						}
						try {
							Object newMsg = filter(messageNode.getContent());
							if (newMsg != null) {
								deliverPush(new Deliver(newMsg,
										new BroadcasterFuture<Object>(newMsg),
										messageNode.getContent()), true);
							}
							
							/*Object newMsg = filter(count);
							if (newMsg != null) {
								deliverPush(new Deliver(newMsg,
										new BroadcasterFuture<Object>(newMsg),
										count), true);
							}*/
						} catch (Throwable t) {
						}
					}
				};

				consumerTag = channel.basicConsume(queueName, true, queueConsumer);
			} catch (Exception e) {

			}
	  }

	public AdminListenerBroadcaster() {
		System.out.println("In AdminListenerBroadcaster constructor");
	}
}
