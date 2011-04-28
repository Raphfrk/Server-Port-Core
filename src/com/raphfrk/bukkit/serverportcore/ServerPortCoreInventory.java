package com.raphfrk.bukkit.serverportcore;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ServerPortCoreInventory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ServerPortCoreItemStack[] slots;
	ServerPortCoreInventory(Player player, boolean strip) {
		
		PlayerInventory i = player.getInventory();

		slots = ServerPortCoreItemStack.getInventory(player);
		
		if(strip) {
			i.clear();
			for(int cnt=0;cnt<4;cnt++) {
				ServerPortCoreItemStack.setItemStack(i, null, true, cnt);
			}
			player.saveData();
		}
		
	}
	
	public ServerPortCoreItemStack[] getSlots() {
		return slots;
	}
	

	public boolean isEmpty() {
		if(slots == null) {
			return true;
		}
		
		return slots.length > 0;
		
	}

}
