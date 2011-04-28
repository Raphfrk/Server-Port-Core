package com.raphfrk.bukkit.serverportcore;

import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.avaje.ebean.EbeanServer;

public class ServerPortCorePlayerListener extends PlayerListener {

	final ServerPortCore p;
	
	ServerPortCorePlayerListener(ServerPortCore p) {
		this.p = p;
	}
	
    public void onPlayerJoin(PlayerJoinEvent event) {

    	p.limboStore.updateFromDatabase(event.getPlayer());
		
    }
	
}
