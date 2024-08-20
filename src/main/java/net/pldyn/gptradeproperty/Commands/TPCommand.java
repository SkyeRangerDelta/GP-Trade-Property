package net.pldyn.gptradeproperty.Commands;

import net.pldyn.gptradeproperty.AccountsConfigHandler;
import net.pldyn.gptradeproperty.GPTradeProperty;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TPCommand implements CommandExecutor {

  @Override
  public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args ) {
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

    try {
      assert currencyItem != null;
      ItemStack currencyStack = new ItemStack( currencyItem, accValue );

      player.getInventory().addItem( currencyStack );
    }
    catch ( Exception e ) {
      player.sendMessage( "Error: Could not deposit currency into your inventory; ask an admin?" );
      return false;
    }

    AccountsConfigHandler.removeAccount( playerUUID );

    return true;
  }
}
