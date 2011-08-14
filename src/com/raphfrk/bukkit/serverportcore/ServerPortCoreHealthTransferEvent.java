package com.raphfrk.bukkit.serverportcore;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ServerPortCoreHealthTransferEvent extends Event {
	
	private static final long serialVersionUID = 1L;

	private final SPHealth health;
	private final String playerName;
	private boolean outbound;
	
	ServerPortCoreHealthTransferEvent(Player player) {
		super("ServerPortCoreHealthTransferEvent");
		health = new SPHealth();
		health.setHealth(player.getHealth());
		health.setName(player.getName());
		this.playerName = player.getName();		
		outbound = true;
	}
	
	public void setOutbound(boolean outbound) {
		this.outbound = outbound;
	}
	
	public boolean getOutbound() {
		return outbound;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public SPHealth getHealth() {
		return health;
	}
	
	public String toString() {
		return playerName + " " + health;
	}
}