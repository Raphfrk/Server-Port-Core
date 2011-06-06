package com.raphfrk.bukkit.serverportcore;

import org.bukkit.entity.Player;

public class TeleportManager {

	ServerPortCore p;

	TeleportManager(ServerPortCore p) {
		this.p = p;
	}

	boolean teleport(String playerName, SPLocation target) {

		SPLocation modifiedTarget = target;

		if(target.getServer() == null) {
			modifiedTarget = new SPLocation(target);
			modifiedTarget.setServer(p.eventLink.getEntryLocation("worlds", target.getWorld()));
		}

		if(modifiedTarget.getServer() == null) {
			return false;
		}

		ServerPortCoreSummonEvent summonEvent = new ServerPortCoreSummonEvent(modifiedTarget, playerName);

		return p.eventLink.sendEvent(modifiedTarget.getServer(), summonEvent);

	}

	void summonRequest(ServerPortCoreSummonEvent summonEvent) {

		if(summonEvent.getTargetGlobalHostname() == null) {
			if(p.globalHostname != null) {
				summonEvent.setTargetGlobalHostname(p.globalHostname);
				String playerLocation = p.eventLink.getEntryLocation("players", summonEvent.getPlayerName());

				if(playerLocation != null) {
					p.eventLink.sendEvent(playerLocation, summonEvent);
				}
			} else {
				p.eventLink.sendMessage(summonEvent.getPlayerName(), "Global hostname not set at target server, unable to teleport");
				p.log("Global hostname not set, unable to summon " + summonEvent.getPlayerName());
			}
		} else {
			Player player = p.getServer().getPlayer(summonEvent.getPlayerName());
			if(player!=null) {

				String targetServer = summonEvent.getTarget().getServer();
				if(targetServer != null) {
					ServerPortCoreInventoryTransferEvent invEvent = new ServerPortCoreInventoryTransferEvent(player);

					if(p.eventLink.sendEvent(targetServer, invEvent)) {
						player.kickPlayer("[Serverport] You have teleported, please connect to : " + summonEvent.getTargetGlobalHostname());
					} else {
						SPItemStack.addInventory(player, invEvent.getServerPortCoreInventory().getSlots());
					}
				}
			}
		}

	}
	
}
