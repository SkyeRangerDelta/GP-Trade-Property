package net.pldyn.gptradeproperty;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Main class of the plugin
 */
public final class GPTradeProperty extends JavaPlugin {

  private static final Logger Log = Logger.getLogger("GP-TradeProperty");

  public static GPTradeProperty instance = null;

  @Override
  public void onEnable() {
    // Plugin startup logic
    Log.info("GP Trade Property Plugin Enabled");

    // Get GP online
    if (getServer().getPluginManager().getPlugin("GriefPrevention") == null) {
      Log.warning("GriefPrevention not found, disabling plugin");
      getServer().getPluginManager().disablePlugin(this);
    }

    ConfigHandler.confInit();
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
    // Register events
    new TPListener().registerEvents();

    Log.info( "GP Trade Property Plugin Initialized" );
  }

  public @NotNull Logger getLogger() {
    return Log;
  }
}
