package net.pldyn.gptradeproperty;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Handles the configuration of the plugin
 */
public class ConfigHandler {
  private static final Logger Log = Logger.getLogger("GP-TradeProperty");

  private static File coreConfigFile;
  private static FileConfiguration coreConfig;

  /**
   * Initialize the configuration
   */
  public static void confInit() {
    // Prep files
    fileInit();
    coreConfig.options().copyDefaults(true);
    saveConfigs();

    createDefaults();
  }

  /**
   * Initialize the files
   */
  private static void fileInit() {
    coreConfigFile = new File(
        Bukkit.getPluginManager()
            .getPlugin( "GP-TradeProperty" )
            .getDataFolder(), "config.yml"
    );

    if ( !coreConfigFile.exists() ) {
      try {
        coreConfigFile.createNewFile();
      } catch ( IOException e ) {
        Log.warning( "Failed to create config file!" );
      }
    }

    coreConfig = YamlConfiguration.loadConfiguration( coreConfigFile );
  }

  /**
   * Create defaults
   */
  private static void createDefaults() {
    coreConfig.addDefault( "enabledWorlds", new ArrayList<String>() );
    saveConfigs();
  }

  /**
   * Save the configuration files
   */
  private static void saveConfigs() {
    try {
      coreConfig.save( coreConfigFile );
    } catch ( IOException e ) {
      Log.warning( "Failed to save config file!" );
    }
  }
}
