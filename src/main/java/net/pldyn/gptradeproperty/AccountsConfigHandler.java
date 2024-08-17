package net.pldyn.gptradeproperty;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/**
 * Handles the "bank accounts" of players. Used as a utility for trade-related functions in the event that
 * standard transactions fail or cannot be completed normally; ie. no inventory space.
 */
public class AccountsConfigHandler {

  private static final Logger Log = Logger.getLogger("GP-TradeProperty");

  private static File accountsConfigFile = new File(GPTradeProperty.pluginDirPath + "accounts.yml");
  private static FileConfiguration accountsConfig;

  public static void initAccountsConfig() {
    // Init files
    createFiles();

    // Set defaults
    setAccountDefaults();
    accountsConfig.options().copyDefaults(true);
    saveAccounts();
  }

  private static void createFiles() {
    accountsConfigFile = new File(
        Bukkit.getPluginManager()
            .getPlugin("GP-TradeProperty")
            .getDataFolder(), "GPTP-Accounts.yml" );

    if (!accountsConfigFile.exists()) {
      try {
        accountsConfigFile.createNewFile();
      } catch (Exception e) {
        Log.warning("Failed to create accounts config!");
      }
    }
  }

  private static void setAccountDefaults() {
    accountsConfig.options().header("This file stores the bank accounts of players. Used for trade-related functions.");
    accountsConfig.addDefault( "accounts", new HashMap< String, Integer >() );
    saveAccounts();
  }

  public static void saveAccounts() {
    try {
      accountsConfig.save(accountsConfigFile);
    } catch (Exception e) {
      Log.warning("Failed to save accounts config!");
    }
  }

  public static void addAccount( UUID player, int amount ) {
    int currentAmount = accountsConfig.getInt( "accounts." + player.toString() );
    accountsConfig.set( "accounts." + player.toString(), currentAmount + amount );
    saveAccounts();
  }

  public static void removeAccount( UUID player ) {
    accountsConfig.set( "accounts." + player.toString(), null );
    saveAccounts();
  }

  public static int getAccount( UUID player ) {
    return accountsConfig.getInt( "accounts." + player.toString() );
  }
}
