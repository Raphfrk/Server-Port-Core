package com.raphfrk.bukkit.serverportcore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.PersistenceException;

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
	
	final ServerPortCoreCustomListener serverPortCoreCustomListener = new ServerPortCoreCustomListener(this);
	
	final ServerPortCorePlayerListener serverPortCorePlayerListener = new ServerPortCorePlayerListener(this);
	
	final TeleportManager teleportManager = new TeleportManager(this);
	
	final LimboStore limboStore = new LimboStore(this);

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
		pm.registerEvent(Type.PLAYER_JOIN, serverPortCorePlayerListener, Priority.Normal, this);
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
		
		setupDatabase();
		
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
	
	
    private void setupDatabase() {
        try {
        	if(getDatabase() == null) {
        		installDDL();
        	} else {
        		getDatabase().find(ServerPortCoreItemStack.class).findRowCount();
        	}
        } catch (PersistenceException ex) {
            log("Creating initial database");
            installDDL();
        }
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(ServerPortCoreItemStack.class);
        return list;
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
				
				if(args.length == 2) {
					ServerPortLocation target = new ServerPortLocation(args[1], null, null, null, null, null, null);

					teleportManager.teleport(((Player)commandSender).getName(), target);
					return true;
				} else if (args.length == 5) {
					try {
						ServerPortLocation target = new ServerPortLocation(args[1], args[2], Double.parseDouble(args[3]), Double.parseDouble(args[3]), Double.parseDouble(args[3]), null, null);
						teleportManager.teleport(((Player)commandSender).getName(), target);
						return true;
					} catch (NumberFormatException nfe) {
						commandSender.sendMessage("Unable to parse command");
						return true;
					}
					
				}
				
			}
			
		}

		return false;

	}
	

	
}

