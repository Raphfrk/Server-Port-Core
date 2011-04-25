package com.raphfrk.bukkit.serverportcore;

import org.bukkit.event.Event;

public class ServerPortCoreSummonEvent extends Event {
	
	private static final long serialVersionUID = 1L;

	private final ServerPortLocation target;
	private final String playerName;

	private String targetGlobalHostname = null;

	
	ServerPortCoreSummonEvent(ServerPortLocation target, String playerName) {
		super("ServerPortCoreSummonEvent");
		this.target = target;
		this.playerName = playerName;
	}
	
	String getPlayerName() {
		return playerName;
	}
	
	ServerPortLocation getTarget() {
		return target;
	}
	
	void setTargetGlobalHostname(String targetGlobalHostname) {
		this.targetGlobalHostname = targetGlobalHostname;
	}
	
	String getTargetGlobalHostname() {
		return targetGlobalHostname;
	}

}
