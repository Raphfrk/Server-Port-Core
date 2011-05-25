package com.raphfrk.bukkit.serverportcore;

import org.bukkit.Server;

import com.raphfrk.bukkit.serverportcoreapi.ServerPortCoreAPI;

public class SPCoreAPI implements ServerPortCoreAPI {
	
	ServerPortCore p;
	
	SPCoreAPI(ServerPortCore p) {
		this.p = p;
	}
	
	public String getProviderName() {
		return "Server Port Core";
	}

	public String getLocalServerName() {
		return p.teleportManager.getServerName();
	}

	public Server getLocalServer() {
		return p.getServer();
	}

}
