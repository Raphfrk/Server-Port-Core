/*******************************************************************************
 * Copyright (C) 2012 Raphfrk
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
