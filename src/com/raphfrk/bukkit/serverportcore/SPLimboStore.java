package com.raphfrk.bukkit.serverportcore;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.avaje.ebean.EbeanServer;

public class SPLimboStore {

	ServerPortCore p;

	SPLimboStore(ServerPortCore p) {
		this.p = p;
	}

	void updateInventoryFromDatabase(Player player) {
		String playerName = player.getName();

		List<SPItemStack> stacks = p.eb.find(SPItemStack.class).where().ieq("playerName", playerName).findList();

		p.eb.beginTransaction();

		Iterator<SPItemStack> itr = stacks.iterator();
		boolean allLoaded = true;
		while(itr.hasNext()) {
			SPItemStack stack = itr.next();
			if(SPItemStack.addItemStack(player, stack)) {
				p.eb.delete(stack);
			} else {
				allLoaded = false;
			}
		}

		if(!allLoaded) {
			player.sendMessage("Some of your items are still in limbo");
		}

		p.eb.commitTransaction();
		player.saveData();
	}

	void writeStacksToDatabase(SPItemStack[] stacks) {

		p.eb.beginTransaction();

		try {
			for(SPItemStack stack : stacks) {
				p.eb.save(stack);
			}
		} finally {
			p.eb.commitTransaction();
		}

	}

	void writeLocationToDatabase(SPLocation location) {

		p.eb.beginTransaction();
		try {

			String playerName = location.getPlayerName();

			SPLocation oldLoc = null;
			do {		
				oldLoc = p.eb.find(SPLocation.class).where().ieq("playerName", playerName).findUnique();
				if(oldLoc != null) {
					p.eb.delete(oldLoc);
				}
			} while (oldLoc != null);

			p.eb.save(location);
		} finally {
			p.eb.endTransaction();
		}



	}

	void updateLocationFromDatabase(Player player) {

		SPLocation loc;
		
		p.eb.beginTransaction();
		try {

		String playerName = player.getName();

		loc = p.eb.find(SPLocation.class).where().ieq("playerName", playerName).findUnique();

		if(loc != null) {
			p.eb.delete(loc);
		}
		} finally {
			p.eb.commitTransaction();
		}

		if(loc != null) {
			Location bukkitLoc = loc.getLocation(p.teleportManager);
			if(bukkitLoc != null) {
				p.teleportManager.safeTeleport(player, bukkitLoc);
			} else {
				player.sendMessage("[ServerPortCore] Unable to teleport to location in database");
			}
		} 


	}

}
