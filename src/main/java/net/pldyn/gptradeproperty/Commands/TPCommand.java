package net.pldyn.gptradeproperty.Commands;

import net.pldyn.gptradeproperty.AccountsConfigHandler;
import net.pldyn.gptradeproperty.GPTradeProperty;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TPCommand implements CommandExecutor, TabExecutor {

  @Override
  public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args ) {
    if ( args.length == 0 ) {
      return false;
    }

    GPTradeProperty.instance.Log.info( "TPCommand: " + Arrays.toString( args ) );

    if ( args[ 0 ].equals( "withdraw" ) ) {
      // Check is player is actual player and not console
      if( !( sender instanceof Player player ) ) {
        sender.sendMessage( "You must be a player to use this command." );
        return true;
      }

      // Check if player has withdraw permission
      if( !sender.hasPermission( "gptradeproperty.tp" ) ) {
        sender.sendMessage( "You do not have permission to use this command." );
        return true;
      }

      // Get player
      UUID playerUUID = player.getUniqueId();

      // Get the account worth and deposit value into their inventory
      Material currencyItem = Material.getMaterial( GPTradeProperty.instance.configHandler.cfgAcceptedCostItemType.toUpperCase() );
      int accValue = AccountsConfigHandler.getAccount( playerUUID );

      if ( accValue <= 0 ) {
        player.sendMessage( "You have no currency to withdraw." );
        return true;
      }

      try {
        assert currencyItem != null;
        ItemStack currencyStack = new ItemStack( currencyItem, accValue );

        player.getInventory().addItem( currencyStack );
      }
      catch ( Exception e ) {
        player.sendMessage( "Error: Could not deposit currency into your inventory; ask an admin?" );
        GPTradeProperty.instance.Log.warning( "Error: Could not deposit currency into player's inventory.\n" + e );
        return true;
      }

      AccountsConfigHandler.removeAccount( playerUUID );

      return true;
    }

    if ( args[ 0 ].equals( "cancelSale" ) ) {
      // Get the claim the player is standing in and attempt to cancel the trade on it
      GPTradeProperty.tradeData.cancelTrade( ( Player ) sender );
    }

    return false;
  }

  @Override
  public @Nullable List<String> onTabComplete( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args ) {
    List<String> completions = new ArrayList<>();

    completions.add( "withdraw" );

    return completions;
  }
}
