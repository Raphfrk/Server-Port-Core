package com.raphfrk.bukkit.serverportcore;

import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

import com.avaje.ebean.EbeanServer;

public class LimboStore {

	ServerPortCore p;
	
	LimboStore(ServerPortCore p) {
		this.p = p;
	}
	
	
	void updateFromDatabase(Player player) {
		String playerName = player.getName();
		
		EbeanServer eb = p.getDatabase();

		List<ServerPortCoreItemStack> stacks = eb.find(ServerPortCoreItemStack.class).where().ieq("playerName", playerName).findList();

		eb.beginTransaction();

		Iterator<ServerPortCoreItemStack> itr = stacks.iterator();
		boolean allLoaded = true;
		while(itr.hasNext()) {
			ServerPortCoreItemStack stack = itr.next();
			if(ServerPortCoreItemStack.addItemStack(player, stack)) {
				eb.delete(stack);
			} else {
				allLoaded = false;
			}
		}

		if(!allLoaded) {
			player.sendMessage("Some of your items are still in limbo");
		}

		eb.commitTransaction();
		player.saveData();
	}
	
	void writeToDatabase(ServerPortCoreItemStack[] stacks) {

		EbeanServer eb = p.getDatabase();
		
		eb.beginTransaction();

		for(ServerPortCoreItemStack stack : stacks) {
			eb.save(stack);
		}
		
		eb.commitTransaction();

	}
	
}
