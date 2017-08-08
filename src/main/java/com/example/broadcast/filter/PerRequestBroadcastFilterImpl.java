package com.example.broadcast.filter;

import javax.inject.Inject;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceSessionFactory;
import org.atmosphere.cpr.PerRequestBroadcastFilter;

public class PerRequestBroadcastFilterImpl implements PerRequestBroadcastFilter{

	private static final String FALSE = "false";
	private static final String TRUE = "true";
	private static final String UPPERCASE = "uppercase";
	@Inject
	AtmosphereResourceSessionFactory atmResourceSessionfactory;
	@Override
	public BroadcastAction filter(String broadcasterId, Object originalMessage,
			Object message) {
		return new BroadcastAction(originalMessage.toString().toUpperCase());
	}

	@Override
	public BroadcastAction filter(String broadcasterId, AtmosphereResource r,
			Object originalMessage, Object message) {
		 Object sessionAttribute =  r.getBroadcaster().getBroadcasterConfig().getAtmosphereConfig().sessionFactory().getSession(r).getAttribute(UPPERCASE);
		 if( sessionAttribute != null){
			 if(sessionAttribute.toString().equalsIgnoreCase(TRUE)){
				 return new BroadcastAction(originalMessage.toString().toUpperCase());
			 }
			 else if(sessionAttribute.toString().equalsIgnoreCase(FALSE)){
				 return new BroadcastAction(originalMessage.toString().toLowerCase());
			 }
		 }
		return new BroadcastAction(originalMessage);
	}

}

