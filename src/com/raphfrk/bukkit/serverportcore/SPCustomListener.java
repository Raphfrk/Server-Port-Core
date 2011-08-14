package com.raphfrk.bukkit.serverportcore;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

public class SPCustomListener extends CustomEventListener {

	final ServerPortCore p;

	SPCustomListener(ServerPortCore p) {
		this.p = p;
	}

	public void onCustomEvent(Event event) {
		if(event instanceof ServerPortCoreSummonEvent) {
			onCustomEvent((ServerPortCoreSummonEvent)event);
		} else if(event instanceof ServerPortCoreInventoryTransferEvent) {
			onCustomEvent((ServerPortCoreInventoryTransferEvent)event);
		} else if(event instanceof ServerPortCoreHealthTransferEvent) {
			onCustomEvent((ServerPortCoreHealthTransferEvent)event);
		}
	}

	public void onCustomEvent(ServerPortCoreSummonEvent summonEvent) {
		
		p.teleportManager.summonRequest(summonEvent);

	}

	public void onCustomEvent(final ServerPortCoreInventoryTransferEvent inventoryEvent) {
		
		p.limboStore.writeStacksToDatabase(inventoryEvent.getServerPortCoreInventory().getSlots());
				
	}
	
	public void onCustomEvent(final ServerPortCoreHealthTransferEvent healthEvent) {
		
		p.limboStore.writeHealthToDatabase(healthEvent.getHealth());
		
	}

}