package com.raphfrk.bukkit.serverportcore;

import org.bukkit.entity.Player;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

import com.raphfrk.bukkit.eventlink.EventLinkMessageEvent;

public class ServerPortCoreCustomListener extends CustomEventListener {

	final ServerPortCore p;

	ServerPortCoreCustomListener(ServerPortCore p) {
		this.p = p;
	}

	public void onCustomEvent(Event event) {
		if(event instanceof ServerPortCoreSummonEvent) {
			onCustomEvent((ServerPortCoreSummonEvent)event);
		}
	}
	
	public void onCustomEvent(ServerPortCoreSummonEvent summonEvent) {

		String globalHostname = summonEvent.getTargetGlobalHostname();
		
		if(summonEvent.getTargetGlobalHostname() == null) {
			if(p.globalHostname != null) {
				summonEvent.setTargetGlobalHostname(p.globalHostname);
				String playerLocation = p.eventLink.getEntryLocation("players", summonEvent.getPlayerName());
				
				if(playerLocation != null) {
					p.eventLink.sendEvent(playerLocation, summonEvent);
				}
			} else {
				EventLinkMessageEvent.sendMessage(summonEvent.getPlayerName(), "Global hostname not set at target server, unable to teleport", p.eventLink);
				p.log("Global hostname not set, unable to summon " + summonEvent.getPlayerName());
			}
		} else {
			Player player = p.getServer().getPlayer( summonEvent.getPlayerName());
			if(player != null) {
				player.kickPlayer("[Serverport] You have teleported, please connect to : " + globalHostname);
			} 
		}
		
	}

}