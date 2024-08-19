package net.pldyn.gptradeproperty.TradeHandling;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pldyn.gptradeproperty.GPTradeProperty;
import net.pldyn.gptradeproperty.MessageHandler;
import net.pldyn.gptradeproperty.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
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

    GPTradeProperty.instance.Log.info( "Updating sign for claim " + claimId );
    GPTradeProperty.instance.Log.info( "PC: " + owner + " Price: " + price + " Sign: " + sign );

    if ( sign.getBlock().getState() instanceof Sign signBlock ) {
      SignSide s = signBlock.getSide( Side.FRONT );

      GPTradeProperty.instance.Log.info( "Using default front." );

      final TextComponent tc1 = Component.text( MessageHandler.getMessage( GPTradeProperty.instance.configHandler.signHeader ) );
      final TextComponent tc2 = Component.text( GPTradeProperty.instance.configHandler.cfgDisplayConfirmed ).color( NamedTextColor.DARK_GREEN );
      final TextComponent tc3 = Component.text( owner != null ? Utilities.getSignString( Objects.requireNonNull( Bukkit.getOfflinePlayer( owner ).getName() ) ) : "SERVER" );

      s.line( 0, tc1 );
      s.line( 1, tc2 );
      s.line( 2, tc3 );

      if ( GPTradeProperty.instance.configHandler.cfgUseCurrencySymbol ) {
        final TextComponent tc4 = Component.text( GPTradeProperty.instance.configHandler.cfgCurrencySymbol + " " + price );
        s.line( 3, tc4 );
      }
      else {
        final TextComponent tc4 = Component.text( "" + price );
        s.line( 3, tc4 );
      }

      GPTradeProperty.instance.Log.info( s.toString() );

      if ( !signBlock.update() ) {
        GPTradeProperty.instance.Log.warning( "Failed to update sign for claim " + claimId );
        return false;
      }

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
