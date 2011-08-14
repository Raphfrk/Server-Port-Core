package com.raphfrk.bukkit.serverportcore;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.raphfrk.bukkit.serverportcoreapi.ServerPortLocation;

public class SPTeleportManager {

	ServerPortCore p;

	SPTeleportManager(ServerPortCore p) {
		this.p = p;
	}

	String getServerName() {
		return p.eventLink.getServerName();
	}

	Server getServer() {
		return p.getServer();
	}

	boolean teleport(String playerName, ServerPortLocation target) {

		ServerPortLocation modifiedTarget = target;

		if(target.getServer() == null) {
			modifiedTarget = (ServerPortLocation)new SPLocation(modifiedTarget);
			modifiedTarget.setServer(p.eventLink.getEntryLocation("worlds", target.getWorld()));
		}

		if(modifiedTarget.getServer() == null) {
			return false;
		}

		ServerPortCoreSummonEvent summonEvent = new ServerPortCoreSummonEvent(modifiedTarget, playerName);

		return p.eventLink.sendEvent(modifiedTarget.getServer(), summonEvent);

	}

	void summonRequest(ServerPortCoreSummonEvent summonEvent) {

		Player player = p.getServer().getPlayer(summonEvent.getPlayerName());
		ServerPortLocation target = summonEvent.getTarget(); 
		Location loc = target.getLocation(p.handler);

		if(player != null && loc != null) {
			p.teleportManager.safeTeleport(player,loc);
			return;
		}

		if(summonEvent.getTargetGlobalHostname() == null) {
			if(p.globalHostname != null) {
				summonEvent.setTargetGlobalHostname(p.globalHostname);
				String playerLocation = p.eventLink.getEntryLocation("players", summonEvent.getPlayerName());

				if(playerLocation != null) {
					if(target.getName() == null) {
						target.setName(summonEvent.getPlayerName());
					}
					p.limboStore.writeLocationToDatabase(summonEvent.getTarget());
					p.eventLink.sendEvent(playerLocation, summonEvent);
				}
			} else {
				p.eventLink.sendMessage(summonEvent.getPlayerName(), "Global hostname not set at target server, unable to teleport");
				p.log("Global hostname not set, unable to summon " + summonEvent.getPlayerName());
			}
		} else {
			if(player!=null) {
				String targetServer = summonEvent.getTarget().getServer();
				if(targetServer != null) {
					ServerPortCoreInventoryTransferEvent invEvent = new ServerPortCoreInventoryTransferEvent(player);
					
					ServerPortCoreHealthTransferEvent healthEvent = new ServerPortCoreHealthTransferEvent(player);
					p.eventLink.sendEvent(targetServer, healthEvent);

					if(p.eventLink.sendEvent(targetServer, invEvent)) {
						player.kickPlayer("[Serverport] You have teleported, please connect to : " + summonEvent.getTargetGlobalHostname());
					} else {
						SPItemStack.addInventory(player, invEvent.getServerPortCoreInventory().getSlots());
					}
				}
			}
		}

	}

	boolean safeTeleport(final Player player, final Location loc) {

		System.out.println("Safe teleporting " + player + " to " + loc);
		
		final int cx = loc.getBlockX()>>4;
		final int cz = loc.getBlockZ()>>4;

		World world = loc.getWorld();
		Chunk chunk = world.getChunkAt(cx, cz);
		
		gridLoad(world, cx, cz);

		if(!world.isChunkLoaded(chunk)) {
			world.loadChunk(chunk);
		}

		Location locCopy = loc.clone();

		locCopy.setY(locCopy.getBlockY());		
		Block block = locCopy.getBlock();
		boolean bottomFilled = block.getTypeId() != 0;
		boolean newBottomFilled;
		block = block.getRelative(BlockFace.UP);

		while((newBottomFilled = (block != null && block.getTypeId() != 0)) || bottomFilled) {
			bottomFilled = newBottomFilled;
			block = block.getRelative(BlockFace.UP);
			locCopy.setY(block.getY()-1);
		}
		
		return player.teleport(locCopy);

	}
	
	void gridLoad(World world, int x, int z) {
		
		world.loadChunk(x, z);
		
		for(int xx=-2;xx<3;xx++) {
			for(int zz=-2;zz<3;zz++) {
				world.loadChunk(x+xx, z+zz);
			}
		}
		
		world.loadChunk(x, z);

		
		
	}
}
