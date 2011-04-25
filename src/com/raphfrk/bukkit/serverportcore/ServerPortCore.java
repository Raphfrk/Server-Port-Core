package com.raphfrk.bukkit.serverportcore;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.raphfrk.bukkit.eventlink.EventLink;
import com.raphfrk.bukkit.eventlink.MiscUtils;
import com.raphfrk.bukkit.eventlink.MyPropertiesFile;

public class ServerPortCore extends JavaPlugin {

	String globalHostname = null;
	
	File pluginDirectory;

	static final String slash = System.getProperty("file.separator");

	final HashSet<String> admins = new HashSet<String>();

	Server server;

	PluginManager pm;
	
	ServerPortCoreCustomListener serverPortCoreCustomListener = new ServerPortCoreCustomListener(this);

	static MiscUtils.LogInstance logger = MiscUtils.getLogger("[ServerPort]");
	
	EventLink eventLink;

	public void onEnable() {

		String name = "Server Port Core";

		pm = getServer().getPluginManager();
		server = getServer();

		log(name + " initialized");

		pluginDirectory = this.getDataFolder();

		if( !readConfig() ) {
			log("Unable to read configs or create dirs, ServerPort core failed to start");
			return;
		}

		pm.registerEvent(Type.CUSTOM_EVENT, serverPortCoreCustomListener, Priority.Normal, this);
		//		pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		//		pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		//		pm.registerEvent(Type.WORLD_LOAD, worldListener, Priority.Normal, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		
		Plugin plugin = pm.getPlugin("EventLink");
		if(!(plugin instanceof EventLink)) {
			log("Unable to connect to EventLink - disabling plugin");
			pm.disablePlugin(this);
		} else {
			log("Connected to eventLink");
			eventLink = (EventLink)plugin;
		}
		
		log(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
		
	}

	public void onDisable() {
	}


	private boolean readConfig() {

		if( !pluginDirectory.exists() ) {
			pluginDirectory.mkdirs();
		}

		MyPropertiesFile pf;
		try {
			pf = new MyPropertiesFile(new File( pluginDirectory , "serverportcore.txt").getCanonicalPath());
		} catch (IOException e) {
			return false;
		}

		pf.load();

		globalHostname = pf.getString("global_hostname", "");

		pf.save();

		return true;

	}

	public void log(String message) {
		logger.log(message);
	}

	public boolean isAdmin(Player player) {
		return eventLink != null && eventLink.isAdmin(player);
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {

		if(!commandSender.isOp()) {
			commandSender.sendMessage("You do not have sufficient user level for this command");
			return true;
		}

		if(command.getName().equals("serverportcore")) {
			
			if(args.length > 1 && args[0].equals("tp")) {
				ServerPortLocation target = new ServerPortLocation(args[1], null, 0, -1, 0, 0, 0);
				
				teleport(((Player)commandSender).getName(), target);
				return true;
				
			}
			
		}

		return false;

	}
	
	boolean teleport(String playerName, ServerPortLocation target) {
		
		ServerPortLocation modifiedTarget = target;
		
		if(target.getServer() == null) {
			modifiedTarget = new ServerPortLocation(target);
			modifiedTarget.setServer(eventLink.getEntryLocation("worlds", target.getWorld()));
		}
		
		if(modifiedTarget.getServer() == null) {
			return false;
		}
		
		ServerPortCoreSummonEvent summonEvent = new ServerPortCoreSummonEvent(modifiedTarget, playerName);
		
		return eventLink.sendEvent(modifiedTarget.getServer(), summonEvent);
		
	}
	
}

