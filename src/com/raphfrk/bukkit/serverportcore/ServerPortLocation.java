package com.raphfrk.bukkit.serverportcore;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

public class ServerPortLocation extends Location implements Serializable {

	private static final long serialVersionUID = 1L;

	String worldName;
	
	private transient World world = null;
	
	public ServerPortLocation(Location loc) {
		super(null, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		World world = loc.getWorld();
		if(world == null) {
			worldName = null;
		} else {
			worldName = world.getName();
		}
		
	}
	
	public ServerPortLocation(ServerPortLocation loc) {
		super(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		setWorld(loc.getWorld());
	}
	
	@Override
	public void setWorld(World world) {
		if(world==null) {
			worldName = null;
		} else {
			worldName = world.getName();
		}
		super.setWorld(world);
	}
	
	public void setWorld(String world) {
		super.setWorld(null);
		worldName = world;
	}
	
	@Override
	public World getWorld() {
		return null;
	}
	
	public World getWorld(Server server) {
		return server.getWorld(getWorldString());
	}
	
	public String getWorldString() {
		return worldName;
	}
	
}
