package net.pldyn.gptradeproperty;

import java.time.Duration;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Utilities
{
  public static boolean makePayment(UUID receiver, UUID giver, double amount, boolean msgSeller, boolean msgBuyer)
  {
    // seller might be null if it is the server
    OfflinePlayer s = receiver != null ? Bukkit.getOfflinePlayer(receiver) : null, b = Bukkit.getOfflinePlayer(giver);
    if( !hasEnoughCost( b, amount ) ) // Check if buyer has enough of the cost (false here) - so cannot buy
    {
      if( b.isOnline() && msgBuyer) // If the buyer is online and the message to the buyer is true
      {
        Messages.sendMessage( b.getPlayer(), GPTradeProperty.instance.messages.msgErrorNoMoneySelf );
      }
      if( s != null && s.isOnline() && msgSeller ) // If the seller is online and the message to the seller is true
      {
        Messages.sendMessage( s.getPlayer(), GPTradeProperty.instance.messages.msgErrorNoMoneyOther, b.getName() );
      }

      return false;
    }

    EconomyResponse resp = GPTradeProperty.econ.withdrawPlayer( b, amount ); // Withdraw the amount from the buyer

    if( !resp.transactionSuccess() ) // If the transaction was not successful
    {
      if( b.isOnline() && msgBuyer ) // If the buyer is online and the message to the buyer is true
      {
        Messages.sendMessage( b.getPlayer(), GPTradeProperty.instance.messages.msgErrorNoWithdrawSelf );
      }

      if( s != null && s.isOnline() && msgSeller ) // If the seller is online and the message to the seller is true
      {
        Messages.sendMessage( b.getPlayer(), GPTradeProperty.instance.messages.msgErrorNoWithdrawOther );
      }

      return false;
    }

    if( s != null ) // If the seller is not null
    {
      resp = GPTradeProperty.econ.depositPlayer( s, amount ); // Deposit the amount to the seller
      if( !resp.transactionSuccess() ) // If the transaction was not successful
      {
        if( b.isOnline() && msgBuyer ) // If the buyer is online and the message to the buyer is true
        {
          Messages.sendMessage( b.getPlayer(), GPTradeProperty.instance.messages.msgErrorNoDepositOther, s.getName() );
        }

        if( s != null && s.isOnline() && msgSeller ) // If the seller is online and the message to the seller is true
        {
          Messages.sendMessage( b.getPlayer(), GPTradeProperty.instance.messages.msgErrorNoDepositSelf, b.getName() );
        }

        GPTradeProperty.econ.depositPlayer( b, amount );

        return false;
      }
    }

    return true;
  }

  public static String getTime(int days, Duration hours, boolean details)
  {
    String time = "";
    if(days >= 7)
    {
      time += (days / 7) + " week" + (days >= 14 ? "s" : "");
    }
    if(days % 7 > 0)
    {
      time += (time.isEmpty() ? "" : " ") + (days % 7) + " day" + (days % 7 > 1 ? "s" : "");
    }
    if((details || days < 7) && hours != null && hours.toHours() > 0)
    {
      time += (time.isEmpty() ? "" : " ") + hours.toHours() + " hour" + (hours.toHours() > 1 ? "s" : "");
    }
    if((details || days == 0) && hours != null && (time.isEmpty() || hours.toMinutes() % 60 > 0))
    {
      time += (time.isEmpty() ? "" : " ") + (hours.toMinutes() % 60) + " min" + (hours.toMinutes() % 60 > 1 ? "s" : "");
    }

    return time;
  }

  public static void transferClaim(Claim claim, UUID buyer, UUID seller)
  {
    // blocks transfer :
    // if transfert is true, the seller will lose the blocks he had
    // and the buyer will get them
    // (that means the buyer will keep the same amount of remaining blocks after the transaction)
    if(claim.parent == null && GPTradeProperty.instance.config.cfgTransferClaimBlocks)
    {
      PlayerData buyerData = GriefPrevention.instance.dataStore.getPlayerData(buyer);
      if(seller != null)
      {
        PlayerData sellerData = GriefPrevention.instance.dataStore.getPlayerData(seller);

        // the seller has to provide the blocks
        sellerData.setBonusClaimBlocks(sellerData.getBonusClaimBlocks() - claim.getArea());
        if (sellerData.getBonusClaimBlocks() < 0)// can't have negative bonus claim blocks, so if need be, we take into the accrued 
        {
          sellerData.setAccruedClaimBlocks(sellerData.getAccruedClaimBlocks() + sellerData.getBonusClaimBlocks());
          sellerData.setBonusClaimBlocks(0);
        }
      }

      // the buyer receive them
      buyerData.setBonusClaimBlocks(buyerData.getBonusClaimBlocks() + claim.getArea());
    }

    // start to change owner
    if(claim.parent == null)
      for(Claim child : claim.children)
      {
        child.clearPermissions();
        child.managers.clear();
      }
    claim.clearPermissions();

    try
    {
      if(claim.parent == null)
        GriefPrevention.instance.dataStore.changeClaimOwner(claim, buyer);
      else
      {
        claim.setPermission(buyer.toString(), ClaimPermission.Build);
      }
    }
    catch (Exception e)// error occurs when trying to change subclaim owner
    {
      e.printStackTrace();
      return;
    }
    GriefPrevention.instance.dataStore.saveClaim(claim);

  }

  public static String getSignString(String str)
  {
    if(str.length() > 16)
      str = str.substring(0, 16);
    return str;
  }

  private static boolean hasEnoughCost( OfflinePlayer player, double cost ) {
    if ( ! player.isOnline() ) {
      return false;
    }

    PlayerInventory inv = player.getPlayer().getInventory();
    int costCount = 0;
    for ( ItemStack item : inv.getContents() ) {
      if ( item != null && item.getType() == Material.DIAMOND ) {
        costCount += item.getAmount();
      }
    }

    return costCount >= cost;
  }
}