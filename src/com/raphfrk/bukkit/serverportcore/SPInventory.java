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
