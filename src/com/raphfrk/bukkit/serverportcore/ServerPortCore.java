package com.raphfrk.bukkit.serverportcore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.Server;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.raphfrk.bukkit.eventlinkapi.EventLinkAPI;
import com.raphfrk.bukkit.serverportcoreapi.ServerPortCoreAPI;
import com.raphfrk.bukkit.serverportcoreapi.ServerPortLocation;

public class ServerPortCore extends JavaPlugin {

	String globalHostname = null;

	File pluginDirectory;

	static final String slash = System.getProperty("file.separator");

	final HashSet<String> admins = new HashSet<String>();

	Server server;

	PluginManager pm;
	ServicesManager sm;

	final SPCustomListener serverPortCoreCustomListener = new SPCustomListener(this);

	final SPPlayerListener serverPortCorePlayerListener = new SPPlayerListener(this);

	final SPTeleportManager teleportManager = new SPTeleportManager(this);

	final SPLimboStore limboStore = new SPLimboStore(this);

	final ServerPortCoreAPI handler = new SPCoreAPI(this);

	EbeanServer eb;

	static MiscUtils.LogInstance logger = MiscUtils.getLogger("[ServerPortCore]");

	EventLinkAPI eventLink;

	public void onEnable() {

		pm = getServer().getPluginManager();
		server = getServer();

		PluginDescriptionFile pdfFile = this.getDescription();

		RegisteredServiceProvider<EventLinkAPI> eventLinkProvider = sm.getRegistration(EventLinkAPI.class);
		eventLink = eventLinkProvider.getProvider();

		if(eventLink == null) {
			log(pdfFile.getName() + " requires EventLink");
			pm.disablePlugin(this);
			return;
		} else {
			log("Connected to Event Link API service");
		}

		pluginDirectory = this.getDataFolder();

		if( !readConfig() ) {
			log("Unable to read configs or create dirs, ServerPort core failed to start");
			pm.disablePlugin(this);
			return;
		}

		pm.registerEvent(Type.CUSTOM_EVENT, serverPortCoreCustomListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_JOIN, serverPortCorePlayerListener, Priority.Normal, this);
		//		pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		//		pm.registerEvent(Type.WORLD_LOAD, worldListener, Priority.Normal, this);

		setupDatabase();

		log(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");

	}

	public void onDisable() {
	}

	@Override
	public void onLoad() {
		sm = getServer().getServicesManager();
		sm.register(ServerPortCoreAPI.class, handler, this, ServicePriority.Normal);
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
		String netherWorlds = pf.getString("nether_worlds","");
		String normalWorlds = pf.getString("normal_worlds","");

		pf.save();

		loadWorlds(normalWorlds, Environment.NORMAL);
		loadWorlds(netherWorlds, Environment.NETHER);

		return true;

	}

	void loadWorlds(String worlds, Environment env) {
		for(String world : worlds.split(",")) {
			if(world.trim().length()==0) {
				continue;
			}
			log("Loading world: " + world);

			getServer().createWorld(world, env);
		}
	}

	List<Class<?>> outdatedClasses = new ArrayList<Class<?>>();

	private void setupDatabase() {
		eb = getDatabase();
		synchronized(outdatedClasses) {
			outdatedClasses.clear();
			boolean refresh = false;
			refresh |= checkTable(SPLocation.class);
			refresh |= checkTable(SPItemStack.class);
			refresh |= checkTable(SPHealth.class);
			if(refresh) {
				log("Updating database");
				installDDL();
				eb = getDatabase();
			}
		}
	}

	boolean checkTable(Class<?> c) {
		synchronized(outdatedClasses) {
			try {
				eb.find(c).findRowCount();
				return false;
			} catch (PersistenceException ex) {
				return true;
			}
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		ArrayList<Class<?>> list = new ArrayList<Class<?>>();
		list.add(SPLocation.class);
		list.add(SPItemStack.class);
		list.add(SPHealth.class);
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

			if(commandSender instanceof Player && args.length > 1 && args[0].equals("tp")) {

				if(args.length == 2) {
					ServerPortLocation target;
					if(eventLink.getEntryLocation("worlds", args[1]) != null) {
						target = new SPLocation(null, args[1], null, null, null, null, null);
					} else if(eventLink.getEntryLocation("worlds", args[1]) != null) {
						target = new SPLocation(args[1], null, null, null, null, null, null);
					} else {
						commandSender.sendMessage(args[1] + " is not a known world or server");
						return true;
					}
					teleportManager.teleport(((Player)commandSender).getName(), target);
					return true;
				} else if (args.length == 6) {
					try {
						ServerPortLocation target = new SPLocation(args[1], args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), null, null);
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

