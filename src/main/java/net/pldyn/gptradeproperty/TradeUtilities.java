package net.pldyn.gptradeproperty;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;
import java.util.UUID;

/**
 * Utility class for trade-related functions. Handles item movement.
 */
public class TradeUtilities {
  /**
   * Withdraws the cost from the buyer.
   */
  public static boolean withdrawCost( OfflinePlayer buyer, int amount) {
    Material currency = Material.getMaterial( GPTradeProperty.instance.configHandler.cfgAcceptedCostItemType.toUpperCase() );

    if ( currency != null && amount > 0 ) {
      ItemStack currencyStack = new ItemStack( currency, amount );
      PlayerInventory buyerInventory = Objects.requireNonNull( buyer.getPlayer() ).getInventory();
      if ( buyerInventory.containsAtLeast( currencyStack, amount ) ) {
        buyerInventory.removeItem( currencyStack );
        return true;
      }
    }

    return false;
  }

  /**
   * Deposits items to the seller. If the seller does not have the inventory space,
   * we update their "bank account" record in the accounts configuration file for them to access later.
   */
  public static boolean depositItems( OfflinePlayer seller, int amount) {
    Material currency = Material.getMaterial( GPTradeProperty.instance.configHandler.cfgAcceptedCostItemType.toUpperCase() );

    try {
      if ( currency != null && amount > 0 ) {
        ItemStack currencyStack = new ItemStack( currency, amount );
        PlayerInventory sellerInventory = Objects.requireNonNull( seller.getPlayer() ).getInventory();
        if ( sellerInventory.firstEmpty() != -1 ) {
          sellerInventory.addItem( currencyStack );
          return true;
        }
      }
    }
    catch ( Exception e ) {
      AccountsConfigHandler.addAccount( seller.getUniqueId(), amount ); // Add the amount to the buyer's account for access later
      return true;
    }

    return false;
  }
}
