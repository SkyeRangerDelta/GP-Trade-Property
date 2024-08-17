package net.pldyn.gptradeproperty;

import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Main class of the plugin
 */
public final class GPTradeProperty extends JavaPlugin {

  public Logger Log = Logger.getLogger("GP-TradeProperty");

  public static GPTradeProperty instance = null;

  public ConfigHandler configHandler;
  public MessageHandler messageHandler;

  public static final String pluginDirPath = "plugins/GP-TradeProperty/";

  @Override
  public void onEnable() {
    // Plugin startup logic
    Log.info("GP Trade Property Plugin Enabled");

    // Get GP online
    if (getServer().getPluginManager().getPlugin("GriefPrevention") == null) {
      Log.warning("GriefPrevention not found, disabling plugin");
      getServer().getPluginManager().disablePlugin(this);
    }

    GPTradeProperty.instance = this;

    doInit();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    Log.info("GP Trade Property Plugin Disabled");
  }

  /**
   * Initialize the plugin
   */
  private void doInit() {
    // Config
    configHandler = new ConfigHandler();
    configHandler.loadConfig();
    configHandler.saveConfig();
    Log.info( "Config loaded." );

    // Messages
    messageHandler = new MessageHandler();
    messageHandler.loadConfig();
    messageHandler.saveConfig();
    Log.info( "Custom messages loaded." );

    // Accounts
    AccountsConfigHandler.initAccountsConfig();

    // Register events
    new TPListener().registerEvents();

    Log.info( "GP Trade Property Plugin Initialized" );
  }

  public @NotNull Logger getLogger() {
    return Log;
  }
}
