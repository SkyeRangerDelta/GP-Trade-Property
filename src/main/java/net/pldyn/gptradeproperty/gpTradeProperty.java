package net.pldyn.gptradeproperty;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Main class of the plugin
 */
public final class gpTradeProperty extends JavaPlugin {

  private static final Logger Log = Logger.getLogger("GP-TradeProperty");
  private GriefPrevention GP;

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
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    Log.info("GP Trade Property Plugin Disabled");
  }

  /**
   * Get instance of the plugin
   */
  public gpTradeProperty getInstance() {
    return this;
  }

  /**
   * Initialize the plugin
   */
  private void doInit(GriefPrevention gp) {
    Log.info( "GP Trade Property Plugin Initialized" );
  }
}
