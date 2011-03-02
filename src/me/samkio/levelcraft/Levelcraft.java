package me.samkio.levelcraft;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import me.samkio.levelcraft.Functions.PlayerFunctions;
import me.samkio.levelcraft.Listeners.LCBlockListener;
import me.samkio.levelcraft.Listeners.LCEntityListener;
import me.samkio.levelcraft.Listeners.LCPlayerListener;
import me.samkio.levelcraft.SamToolbox.DataMySql;
import me.samkio.levelcraft.SamToolbox.DataSqlite;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Levelcraft extends JavaPlugin {
	@SuppressWarnings("unused")
	private final Admin Admin = new Admin(this);
	private static final Logger log = Logger.getLogger("Minecraft");
	private final LCPlayerListener playerListener = new LCPlayerListener(this);
	private final LCBlockListener blockListener = new LCBlockListener(this);
	private final LCEntityListener entityListener = new LCEntityListener(this);
	public static String maindirectory = "LevelCraft/";
	public static String datadirectory = "Experience/";
	public static String configdirectory = "Config/";
	public static File WCExpFile = new File(maindirectory + datadirectory
			+ "WoodCutting.exp");
	public static File MiExpFile = new File(maindirectory + datadirectory
			+ "Mining.exp");
	public static File SlayExpFile = new File(maindirectory + datadirectory
			+ "Slaying.exp");
	public static File RangeExpFile = new File(maindirectory + datadirectory
			+ "Ranging.exp");
	public static File FisticuffsExpFile = new File(maindirectory + datadirectory
			+ "Fisticuffs.exp");
	public static File ArcherExpFile = new File(maindirectory + datadirectory
			+ "Archer.exp");

	public Levelcraft(PluginLoader pluginLoader, Server instance,
			PluginDescriptionFile desc, File folder, File plugin,
			ClassLoader cLoader) {
		super();
	}

	@Override
	public void onDisable() {
		System.out.println("Levelcraft Disabled");

	}

	@Override
	public void onEnable() {
		load();
		registerEvents();
         //Enable
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is enabled!");

	}
/*
 * I have no idea what you guys are trying to do, but I took this out because commands don't work now -Bill
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    	if (sender instanceof Player){
        	PlayerFunctions.checkAccount((Player) sender);    		
    	}
    	if (commandLabel.equalsIgnoreCase("level") | commandLabel.equalsIgnoreCase("lvl")) {
    		return true;
    	}
    	return false;
    }
*/    
//    public boolean onCommand(CommandSender sender, Command cmd,
//    		   String commandLabel, String[] args) {
//    		  if ((commandLabel.equalsIgnoreCase("yt") || commandLabel
//    		    .equalsIgnoreCase("yedit")) && args.length > 0) {
//    		   if (Permissions.has((Player) sender, "yeditor.editor")) {
//    		    commands.command(sender, cmd, commandLabel, args);
//    		   }
//
//    		   return true;
//    		  }
//    		  return false;
//
//    		 }
	
	void load() {
		new File(maindirectory).mkdirs();
		new File(maindirectory + datadirectory).mkdirs();
		new File(maindirectory + configdirectory).mkdirs();

		Settings.loadMain();
		Settings.loadWoodcut();
		Settings.loadSlayer();
		Settings.loadMine();
		Settings.loadWhitelist();
		Settings.loadRange();
		Settings.loadFisticuffs();
		Settings.loadArcher();
		
		if (Settings.database.equalsIgnoreCase("flatfile")) {
			
			if (!WCExpFile.exists() || !MiExpFile.exists()
					|| !SlayExpFile.exists() || !RangeExpFile.exists() || !FisticuffsExpFile.exists())
				try {
					WCExpFile.createNewFile();
					MiExpFile.createNewFile();
					SlayExpFile.createNewFile();
					RangeExpFile.createNewFile();
					FisticuffsExpFile.createNewFile();
					ArcherExpFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		} else if (Settings.database.equalsIgnoreCase("mysql")) {
			DataMySql.PrepareDB();
		} else if (Settings.database.equalsIgnoreCase("sqlite")) {
			DataSqlite.PrepareDB();
		}else{
			log.severe("[Levelcraft] Nowhere to save data! Edit MainConfig Database.");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
//add the following event back, remove when onCommand stuff is complete - Bill		
		pm.registerEvent(Event.Type.PLAYER_COMMAND, this.playerListener,
				         Event.Priority.Normal, this); 	
		pm.registerEvent(Event.Type.ENTITY_DAMAGED,
				this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_TARGET,
				this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_DAMAGED, blockListener,
				Event.Priority.Normal, this);
	}

}
