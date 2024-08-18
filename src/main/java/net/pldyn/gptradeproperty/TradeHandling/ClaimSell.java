package net.pldyn.gptradeproperty.TradeHandling;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.pldyn.gptradeproperty.GPTradeProperty;
import net.pldyn.gptradeproperty.MessageHandler;
import net.pldyn.gptradeproperty.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimSell extends TradeTransaction {
  public ClaimSell( Claim claim, Player pc, int price, Location sign ) {
    super( claim, pc, price, sign );
  }

  public ClaimSell( Map<String, Object> map ) {
    super( map );
  }

  @Override
  public boolean update() {

    if ( sign.getBlock().getState() instanceof Sign ) {
      Sign s = ( Sign ) sign.getBlock().getState();

      // Check if this is a trade sign
      List<String> lines = new ArrayList<>();
      for ( int i = 0; i < 4; i++ ) {
        lines.add( PlainTextComponentSerializer.plainText().serialize( Objects.requireNonNull( s.line( i ) ) ) );
      }

      //TODO: Refactor to account for multiple sides
      s.setLine( 0, MessageHandler.getMessage( GPTradeProperty.instance.configHandler.signHeader ) );
      s.setLine( 1, ChatColor.DARK_GREEN + GPTradeProperty.instance.configHandler.cfgDisplayConfirmed );
      s.setLine( 2, owner != null ? Utilities.getSignString( Objects.requireNonNull( Bukkit.getOfflinePlayer( owner ).getName() ) ) : "SERVER" );

      if ( GPTradeProperty.instance.configHandler.cfgUseCurrencySymbol ) {
        s.setLine( 3, GPTradeProperty.instance.configHandler.cfgCurrencySymbol + " " + (int) Math.round( price ) );
      }
      else {
        s.setLine( 3, "" + price );
      }

      s.update( true );

    }
    else {
      GPTradeProperty.tradeData.cancelTrade( this );
    }

    return false;
  }

  @Override
  public boolean tryCancelTrade( Player pc, boolean force ) {
    GPTradeProperty.tradeData.cancelTrade( this );
    return true;
  }

  @Override
  public void interact( Player player ) {
    Claim claim = GriefPrevention.instance.dataStore.getClaimAt( sign, false, null );

    if ( claim == null ) {
      MessageHandler.sendMessage( player, GPTradeProperty.instance.messageHandler.msgErrorClaimNonExistent );
      GPTradeProperty.tradeData.cancelTrade( this );
      return;
    }

    String cType = claim.parent == null ? "claim" : "subclaim";
    String claimDisplay = claim.parent == null ?
        GPTradeProperty.instance.messageHandler.keywordClaim :
        GPTradeProperty.instance.messageHandler.keywordSubclaim;

    if ( player.getUniqueId().equals( owner ) ) {
      MessageHandler.sendMessage( player, GPTradeProperty.instance.messageHandler.msgErrorClaimAlreadyOwner, claimDisplay );
      return;
    }

    if ( claim.parent == null && owner != null && !owner.equals( claim.ownerID ) ) {
      MessageHandler.sendMessage( player, GPTradeProperty.instance.messageHandler.msgErrorClaimNotSoldByOwner, claimDisplay );
      GPTradeProperty.tradeData.cancelTrade( this );
      return;
    }

    //TODO: Implement permissions
//    if ( !player.hasPermission( "gptradeproperty.trade" ) ) {
//      MessageHandler.sendMessage( player, GPTradeProperty.instance.messageHandler.msgErrorNoPermission );
//      return;
//    }

    // Need enough claims to buy
    if ( cType.equalsIgnoreCase( "claim" ) && !GPTradeProperty.instance.configHandler.cfgTransferClaimBlocks &&
      GriefPrevention.instance.dataStore.getPlayerData( player.getUniqueId() ).getRemainingClaimBlocks() < claim.getArea() ) {
      int remaining = GriefPrevention.instance.dataStore.getPlayerData( player.getUniqueId() ).getRemainingClaimBlocks();
      int area = claim.getArea();
      MessageHandler.sendMessage( player, GPTradeProperty.instance.messageHandler.msgErrorNoClaimBlocks,
          area + "",
          remaining + "",
          ( area - remaining ) + "" );
      return;
    }

    if ( Utilities.makePayment( owner, player.getUniqueId(), price, false, true ) ) {
      Utilities.transferClaim( claim, player.getUniqueId(), owner );

      Location playerLoc = player.getLocation();

      if ( claim.parent != null || claim.ownerID.equals( player.getUniqueId() ) ) {
        String location = "[" + player.getLocation().getWorld() + ", " +
            "X: " + playerLoc.getBlockX() + ", " +
            "Y: " + playerLoc.getBlockY() + ", " +
            "Z: " + playerLoc.getBlockZ() + "]";

        MessageHandler.sendMessage( player, GPTradeProperty.instance.messageHandler.msgClaimBuyerSold,
            claimDisplay,
            price + "");

        //TODO: Logs?

        if ( GPTradeProperty.instance.configHandler.cfgMessageOwner && owner != null ) {
          OfflinePlayer oldOwner = Bukkit.getOfflinePlayer( owner );
          if ( oldOwner.isOnline() ) {
            MessageHandler.sendMessage( ( CommandSender ) oldOwner, GPTradeProperty.instance.messageHandler.msgClaimOwnerSold,
                claimDisplay,
                price + "",
                location );
          }
        }
      }
      else {
        MessageHandler.sendMessage( player, GPTradeProperty.instance.messageHandler.msgErrorUnexpected );
        return;
      }

      GPTradeProperty.tradeData.cancelTrade( this );
    }
  }

  @Override
  public void preview( Player player ) {
    Claim claim = GriefPrevention.instance.dataStore.getClaimAt( sign, false, null );

    String cType = claim.parent == null ? "claim" : "subclaim";
    String claimDisplay = claim.parent == null ?
        GPTradeProperty.instance.messageHandler.keywordClaim :
        GPTradeProperty.instance.messageHandler.keywordSubclaim;
    String msg = MessageHandler.getMessage( GPTradeProperty.instance.messageHandler.msgClaimInfoSellHeader + "\n" );

    msg += MessageHandler.getMessage( GPTradeProperty.instance.messageHandler.msgClaimInfoSellGeneral,
        claimDisplay,
        "" + price );

    if ( cType.equalsIgnoreCase( "claim" ) ) {
      msg += MessageHandler.getMessage( GPTradeProperty.instance.messageHandler.msgClaimInfoOwner,
          claim.getOwnerName()) + "\n";
    }
    else {
      msg += MessageHandler.getMessage( GPTradeProperty.instance.messageHandler.msgClaimInfoMainOwner,
          claim.parent.getOwnerName()) + "\n";

      msg += MessageHandler.getMessage( GPTradeProperty.instance.messageHandler.msgInfoClaimNote ) + "\n";
    }

    MessageHandler.sendMessage( player, msg );
  }

  @Override
  public void setOwner( UUID owner ) {
    this.owner = owner;
  }

  @Override
  public void messageData( CommandSender cs ) {
    Claim claim = GriefPrevention.instance.dataStore.getClaimAt( sign, false, null );

    if ( claim == null ) {
      tryCancelTrade( null, true );
      return;
    }

    Location claimLoc = claim.getLesserBoundaryCorner();
    String location = "[" + claimLoc.getWorld().getName() + ", " +
        "X: " + claimLoc.getBlockX() + ", " +
        "Y: " + claimLoc.getBlockY() + ", " +
        "Z: " + claimLoc.getBlockZ() + "]";

    MessageHandler.sendMessage( cs, GPTradeProperty.instance.messageHandler.msgClaimInfoSellSingleLine,
        claim.getArea(),
        location,
        "" + price );
  }
}
