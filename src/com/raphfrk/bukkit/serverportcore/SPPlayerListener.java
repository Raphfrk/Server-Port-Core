package com.raphfrk.bukkit.serverportcore;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class SPPlayerListener extends PlayerListener {

	final ServerPortCore p;

	SPPlayerListener(ServerPortCore p) {
		this.p = p;
	}

	public void onPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();
		
		p.limboStore.updateInventoryFromDatabase(player);
		p.limboStore.updateHealthFromDatabase(player);
		p.limboStore.updateLocationFromDatabase(player);
		
	}

}
