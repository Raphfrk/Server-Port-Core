package com.raphfrk.bukkit.serverportcore;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.raphfrk.bukkit.serverportcoreapi.ServerPortLocation;

public class SPLimboStore {

	ServerPortCore p;

	SPLimboStore(ServerPortCore p) {
		this.p = p;
	}

	void updateInventoryFromDatabase(Player player) {
		String playerName = player.getName();

		List<SPItemStack> stacks = p.eb.find(SPItemStack.class).where().ieq("name",   playerName).findList();

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
	
	void writeHealthToDatabase(SPHealth health) {
		
		p.eb.beginTransaction();
		try {

			String playerName = health.getName();

			SPHealth oldHealth = null;
			do {		
				oldHealth = p.eb.find(SPHealth.class).where().ieq("name", playerName).findUnique();
				if(oldHealth != null) {
					System.out.println("Deleting old version");
					p.eb.delete(oldHealth);
				}
			} while (oldHealth != null);

			p.eb.save(health);


		} finally {
			p.eb.commitTransaction();
		}
	}
	
	void updateHealthFromDatabase(Player player) {

		SPHealth health;

		String playerName =  player.getName();

		health = p.eb.find(SPHealth.class).where().ieq("name",   playerName).findUnique();

		System.out.println("Updating from database: " + health);
		
		if(health != null) {
			p.eb.delete(health);

			int healthValue = health.getHealth();
			if(healthValue < 0) {
				healthValue = 0;
			} else if (healthValue > 20){
				healthValue = 20;
			}
			player.setHealth(healthValue);
		} 

	}

	void writeLocationToDatabase(ServerPortLocation location) {

		p.eb.beginTransaction();
		try {

			String playerName = location.getName();

			SPLocation oldLoc = null;
			do {		
				oldLoc = p.eb.find(SPLocation.class).where().ieq("name", playerName).findUnique();
				if(oldLoc != null) {
					System.out.println("Deleting old version");
					p.eb.delete(oldLoc);
				}
			} while (oldLoc != null);

			SPLocation toSave = new SPLocation(location);

			toSave.setName( location.getName());

			p.eb.save(toSave);


		} finally {
			p.eb.commitTransaction();
		}



	}

	void updateLocationFromDatabase(Player player) {

		SPLocation loc;

		String playerName =  player.getName();

		loc = p.eb.find(SPLocation.class).where().ieq("name",   playerName).findUnique();

		if(loc != null) {
			p.eb.delete(loc);
		}

		if(loc != null) {
			Location bukkitLoc = loc.getLocation(p.handler);
			if(bukkitLoc != null) {
				p.teleportManager.safeTeleport(player, bukkitLoc);
			} else {
				player.sendMessage("[ServerPortCore] Unable to teleport to location in database");
			}
		} 


	}

}
