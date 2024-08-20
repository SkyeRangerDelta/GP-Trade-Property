package net.pldyn.gptradeproperty;

import net.pldyn.gptradeproperty.Commands.TPCommand;
import net.pldyn.gptradeproperty.TradeHandling.ClaimSell;
import net.pldyn.gptradeproperty.TradeHandling.TradeData;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Main class of the plugin
 */
public final class GPTradeProperty extends JavaPlugin {

  public Logger Log = Logger.getLogger("GP-TradeProperty");

  public static GPTradeProperty instance = null;

  public ConfigHandler configHandler;
  public MessageHandler messageHandler;

  public static TradeData tradeData = null;

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

    Material currencyCheck = Material.getMaterial( configHandler.cfgAcceptedCostItemType.toUpperCase() );
    if ( currencyCheck == null ) {
      Log.warning( "Invalid currency item in config. Disabling plugin." );
      getServer().getPluginManager().disablePlugin( this );
      return;
    }

    // Messages
    messageHandler = new MessageHandler();
    messageHandler.loadConfig();
    messageHandler.saveConfig();
    Log.info( "Custom messages loaded." );

    // Classes
    ConfigurationSerialization.registerClass( ClaimSell.class );

    // Trade data
    GPTradeProperty.tradeData = new TradeData();

    // Accounts
    AccountsConfigHandler.initAccountsConfig();

    // Commands
    Objects.requireNonNull(
        getCommand( "tradeproperty" ) )
        .setExecutor( new TPCommand() );

    // Register events
    new TPListener().registerEvents();

    Log.info( "GP Trade Property Plugin Initialized" );
  }

  public @NotNull Logger getLogger() {
    return Log;
  }
}
