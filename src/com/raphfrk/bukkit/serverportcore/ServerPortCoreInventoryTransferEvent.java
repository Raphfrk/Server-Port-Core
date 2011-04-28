package com.raphfrk.bukkit.serverportcore;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ServerPortCoreInventoryTransferEvent extends Event {
	
	private static final long serialVersionUID = 1L;

	private final ServerPortCoreInventory inventory;
	private final String playerName;
	private boolean outbound;
	
	ServerPortCoreInventoryTransferEvent(Player player) {
		super("ServerPortCoreInventoryTransferEvent");
		inventory = new ServerPortCoreInventory(player, true);
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
	
	public ServerPortCoreInventory getServerPortCoreInventory() {
		return inventory;
	}
	
}
