package com.Jacksonnn.DCCore;

import com.Jacksonnn.DCCore.BannedWords.BannedWordsCommand;
import com.Jacksonnn.DCCore.BannedWords.BannedWordsListener;
import com.Jacksonnn.DCCore.Broadcast.BroadcastCommand;
import com.Jacksonnn.DCCore.ChatSensor.ChatListener;
import com.Jacksonnn.DCCore.Configuration.ConfigManager;
import com.Jacksonnn.DCCore.DiamondLuck.DiamondLuck;
import com.Jacksonnn.DCCore.DiamondLuck.ResponseListener;
import com.Jacksonnn.DCCore.Events.EventCommand;
import com.Jacksonnn.DCCore.Events.PlayerEvents.PlayerKillEvent;
import com.Jacksonnn.DCCore.Events.PlayerEvents.PlayerLeaveEvent;
import com.Jacksonnn.DCCore.OverrideCommands.*;
import com.Jacksonnn.DCCore.QuickDeposit.QuickDepositListener;
import com.Jacksonnn.DCCore.RandomTP.RandomTP;
import com.Jacksonnn.DCCore.Rankup.GuestQuizListener;
import com.Jacksonnn.DCCore.Rankup.PlayTime;
import com.Jacksonnn.DCCore.Rankup.Ranks;
import com.Jacksonnn.DCCore.Rankup.Rankup;
import com.Jacksonnn.DCCore.Spawners.SpawnerListener;
import com.Jacksonnn.DCCore.StaffChannels.*;
import com.Jacksonnn.DCCore.StaffCounts.StaffCountCommand;
import com.Jacksonnn.DCCore.StaffCounts.StaffNotification;
import com.Jacksonnn.DCCore.Storage.DatabaseManager;
import com.Jacksonnn.DCCore.Storage.Mysql;
import com.Jacksonnn.DCCore.Storage.SqlQueries;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class DCCore extends JavaPlugin {
	
	public static DCCore plugin;
	private PluginManager pm = Bukkit.getServer().getPluginManager();
	private DatabaseManager databaseManager;

	public void onEnable() {
		plugin = this;
		registerListeners();
		registerCommands();
		new ConfigManager();
		Bukkit.getServer().getLogger().info("DCCore has successfully been enabled!");

		databaseManager = new DatabaseManager(this);
		try {
			databaseManager.init();
		} catch (SQLException e) {
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}

		try {
			installDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void installDatabase() throws SQLException {
		if (databaseManager.getDatabase() instanceof Mysql) {
			databaseManager.getDatabase().getConnection().createStatement().execute(SqlQueries.CREATE_TOURNAMENTS.getMysqlQuery());
		} else {
			databaseManager.getDatabase().getConnection().createStatement().execute(SqlQueries.CREATE_TOURNAMENTS.getSqliteQuery());
		}
	}
	
	public void onDisable() {
		Bukkit.getServer().getLogger().info("DCCore has successfully been disabled!");
	}
	
/*
 * REGISTER LISTENERS AND COMMANDS
 */
	private void registerListeners() {
		pm.registerEvents(new SpawnerListener(), this);
		pm.registerEvents(new QuickDepositListener(), this);
		pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new ResponseListener(), this);
        pm.registerEvents(new PlayerLeaveEvent(), this);
        pm.registerEvents(new PlayerKillEvent(), this);
        pm.registerEvents(new GuestQuizListener(), this);
        pm.registerEvents(new onChatEvent(), this);
        pm.registerEvents(new onLeaveEvent(), this);
        pm.registerEvents(new StaffNotification(), this);
        pm.registerEvents(new Vanish(), this);
        pm.registerEvents(new BannedWordsListener(), this);
	}
	
	private void registerCommands() {
		this.getCommand("broadcast").setExecutor(new BroadcastCommand());
		this.getCommand("bannedwords").setExecutor(new BannedWordsCommand());
		this.getCommand("dccore").setExecutor(new CoreCommands());
		this.getCommand("rankup").setExecutor(new Rankup());
		this.getCommand("playtime").setExecutor(new PlayTime());
		this.getCommand("ranks").setExecutor(new Ranks());
		this.getCommand("diamondluck").setExecutor(new DiamondLuck());
		this.getCommand("randomtp").setExecutor(new RandomTP());
		this.getCommand("b").setExecutor(new PKAlias());
		this.getCommand("vote").setExecutor(new Vote());
		this.getCommand("forums").setExecutor(new Forums());
		this.getCommand("donate").setExecutor(new Donate());
		this.getCommand("website").setExecutor(new Website());
		this.getCommand("bendinghelp").setExecutor(new BendingHelp());
		this.getCommand("avatar").setExecutor(new Avatar());
		this.getCommand("lightspirit").setExecutor(new LightSpirit());
		this.getCommand("darkspirit").setExecutor(new DarkSpirit());
		this.getCommand("bendingfix").setExecutor(new BendingFix());
		this.getCommand("headofstaff").setExecutor(new HoSCommand());
		this.getCommand("managers").setExecutor(new ManagersCommand());
		this.getCommand("moderators").setExecutor(new ModeratorsCommand());
		this.getCommand("staffchat").setExecutor(new StaffChatCommand());
		this.getCommand("artists").setExecutor(new ArtistCommand());
		this.getCommand("staffcount").setExecutor(new StaffCountCommand());

		//EVENTS COMMAND
		EventCommand eventCommand = new EventCommand(this);
		this.getCommand("dcevents").setExecutor(eventCommand);
		this.getCommand("dcevents").setTabCompleter(eventCommand);
	}

	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
}

