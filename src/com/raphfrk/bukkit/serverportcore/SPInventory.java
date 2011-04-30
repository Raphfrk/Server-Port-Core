package com.raphfrk.bukkit.serverportcore;

import java.io.Serializable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class SPInventory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SPItemStack[] slots;
	SPInventory(Player player, boolean strip) {
		
		PlayerInventory i = player.getInventory();

		slots = SPItemStack.getInventory(player);
		
		if(strip) {
			i.clear();
			for(int cnt=0;cnt<4;cnt++) {
				SPItemStack.setItemStack(i, null, true, cnt);
			}
			player.saveData();
		}
		
	}
	
	public SPItemStack[] getSlots() {
		return slots;
	}
	

	public boolean isEmpty() {
		if(slots == null) {
			return true;
		}
		
		return slots.length > 0;
		
	}

}
