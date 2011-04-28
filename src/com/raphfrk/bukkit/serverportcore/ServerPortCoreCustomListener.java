package com.raphfrk.bukkit.serverportcore;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

public class ServerPortCoreCustomListener extends CustomEventListener {

	final ServerPortCore p;

	ServerPortCoreCustomListener(ServerPortCore p) {
		this.p = p;
	}

	public void onCustomEvent(Event event) {
		if(event instanceof ServerPortCoreSummonEvent) {
			onCustomEvent((ServerPortCoreSummonEvent)event);
		} else if(event instanceof ServerPortCoreInventoryTransferEvent) {
			onCustomEvent((ServerPortCoreInventoryTransferEvent)event);
		}
	}

	public void onCustomEvent(ServerPortCoreSummonEvent summonEvent) {
		
		p.teleportManager.summonRequest(summonEvent);

	}

	public void onCustomEvent(final ServerPortCoreInventoryTransferEvent inventoryEvent) {
		
		p.limboStore.writeToDatabase(inventoryEvent.getServerPortCoreInventory().getSlots());
				
	}

}