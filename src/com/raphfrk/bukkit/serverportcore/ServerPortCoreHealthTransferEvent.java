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

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ServerPortCoreHealthTransferEvent extends Event {
	
	private static final long serialVersionUID = 1L;

	private final SPHealth health;
	private final String playerName;
	private boolean outbound;
	
	ServerPortCoreHealthTransferEvent(Player player) {
		super("ServerPortCoreHealthTransferEvent");
		health = new SPHealth();
		health.setHealth(player.getHealth());
		health.setName(player.getName());
		this.playerName = player.getName();		
		outbound = true;
	}
	
	public void setOutbound(boolean outbound) {
		this.outbound = outbound;
	}
	
	public boolean getOutbound() {
		return outbound;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public SPHealth getHealth() {
		return health;
	}
	
	public String toString() {
		return playerName + " " + health;
	}
}