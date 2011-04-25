package com.raphfrk.bukkit.serverportcore;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

public class ServerPortLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String server;
	private String world;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;

	public ServerPortLocation(String serverName, Location loc) {
		this(
				serverName, 
				(loc.getWorld()==null)?null:(loc.getWorld().getName()), 
				loc.getX(),
				loc.getY(),
				loc.getZ(),
				loc.getPitch(),
				loc.getYaw()
				);
	}

	public ServerPortLocation(ServerPortLocation loc) {
		this(loc.getServer(), loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public ServerPortLocation(String server, String world, double x, double y, double z, float yaw, float pitch) {
		this.server = server;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getWorld() {
		return world;
	}

	public String getServer() {
		return server;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return this.x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return this.y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getZ() {
		return this.z;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getYaw() {
		return this.yaw;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getPitch() {
		return this.pitch;
	}
	
	Location getLocation() {
		return new Location(null, x, y, z, yaw, pitch);
	}
	
	Location getLocation(Server server) {
		World world = server.getWorld(this.world);
		if(world != null) {
			return new Location(world, x, y, z, yaw, pitch);
		} else {
			return null;
		}
	}


}
