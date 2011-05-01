package com.raphfrk.bukkit.serverportcore;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.bukkit.Location;
import org.bukkit.World;

@Entity
public class SPLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	
	@Column(unique=true, nullable=false)
	//fdjkshfsdhkfsdjh // Add "warpName" as another field 
	private String playerName;
	private String server;
	private String world;
	private Double x;
	private Double y;
	private Double z;
	private Float pitch;
	private Float yaw;
	
	public SPLocation() {
	}

	public SPLocation(String serverName, Location loc) {
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

	public SPLocation(SPLocation loc) {
		this(loc.getServer(), loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public SPLocation(String server, String world, Double x, Double y, Double z, Float yaw, Float pitch) {
		this.server = server;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getPlayerName() {
		return playerName;
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

	public void setX(Double x) {
		this.x = x;
	}

	public Double getX() {
		return this.x;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Double getY() {
		return this.y;
	}

	public void setZ(Double z) {
		this.z = z;
	}

	public Double getZ() {
		return this.z;
	}

	public void setYaw(Float yaw) {
		this.yaw = yaw;
	}

	public Float getYaw() {
		return this.yaw;
	}

	public void setPitch(Float pitch) {
		this.pitch = pitch;
	}

	public Float getPitch() {
		return this.pitch;
	}
	
	Location getLocation() {
		return new Location(null, x, y, z, yaw, pitch);
	}
	
	Location getLocation(SPTeleportManager teleportManager) {

		String pluginServerName = teleportManager.getServerName();
		if(server != null && !server.equals(pluginServerName)) {
			return null;
		}
		World bukkitWorld = null;
		if(world != null) {
			bukkitWorld = teleportManager.getServer().getWorld(this.world);
			if(bukkitWorld == null) {
				return null;
			}
		}
		
		if(bukkitWorld == null) {
			bukkitWorld = teleportManager.p.getServer().getWorlds().get(0);
		}
		
		if(x==null || y==null || z==null) {
			Location spawn = bukkitWorld.getSpawnLocation();
			spawn.setX(spawn.getX() + 0.5);
			spawn.setZ(spawn.getZ() + 0.5);
			return spawn;
		} else if(pitch == null || yaw == null) {
			return new Location(bukkitWorld, x, y, z);
		} else {
			return new Location(bukkitWorld, x, y, z, yaw, pitch);
		}
	}
	
}
