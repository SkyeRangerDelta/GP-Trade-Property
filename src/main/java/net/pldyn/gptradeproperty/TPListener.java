package net.pldyn.gptradeproperty;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * EventListener for sign events. On sign change, check for claims, proper sign placement, sign content, and if it's meant for claim trading,
 * conduct the logic accordingly. Also listen for player interaction events - if it's with a trade sign, check for the player
 * interacting - if it's not the owner, conduct the transaction of the player has the asking cost in their inventory.
 */
public class TPListener implements Listener {

  private Logger Log = GPTradeProperty.instance.getLogger();

  /**
   * Register events
   */
  public void registerEvents() {
    PluginManager pm = GPTradeProperty.instance.getServer().getPluginManager();
    pm.registerEvents( this, GPTradeProperty.instance );
  }

  /**
   * onSignChange event
   */
  @EventHandler
  public void onSignChange( SignChangeEvent ev ) {


    // Check if this is a trade sign
    List<String> lines = new ArrayList<>();
    for ( int i = 0; i < 4; i++ ) {
      lines.add( PlainTextComponentSerializer.plainText().serialize( Objects.requireNonNull( ev.line( i ) ) ) );
    }

    Log.info( "Sign Text: " + lines );

    // Return if not a trade sign
    if ( !GPTradeProperty.instance.configHandler.cfgSellSigns.contains( lines.getFirst().toLowerCase() ) ) return;

    Log.info( "Trade sign detected " );

    Player pc = ev.getPlayer();
    Location loc = ev.getBlock().getLocation();

    Claim signClaim = GriefPrevention.instance.dataStore.getClaimAt( loc, true, null );

    // Check if the sign is in a claim
    if ( signClaim == null ) {
      pc.sendMessage( "Sign must be placed in a claim" );
      ev.setCancelled( true );
      ev.getBlock().breakNaturally();
      return;
    }

    if ( GPTradeProperty.tradeData.anyTransaction( signClaim ) ) {
      MessageHandler.sendMessage( pc, GPTradeProperty.instance.messageHandler.msgErrorSignOngoingTrade );
      ev.setCancelled( true );
      ev.getBlock().breakNaturally();
      return;
    }

    if ( GPTradeProperty.tradeData.anyTransaction( signClaim.parent ) ) {
      MessageHandler.sendMessage( pc, GPTradeProperty.instance.messageHandler.msgErrorSignOngoingTradeParent );
      ev.setCancelled( true );
      ev.getBlock().breakNaturally();
      return;
    }

    for ( Claim c : signClaim.children ) {
      if ( GPTradeProperty.tradeData.anyTransaction( c ) ) {
        MessageHandler.sendMessage( pc, GPTradeProperty.instance.messageHandler.msgErrorSignOngoingTradeSubclaim );
        ev.setCancelled( true );
        ev.getBlock().breakNaturally();
        return;
      }
    }

    if ( !GPTradeProperty.instance.configHandler.cfgEnableSell ) {
      MessageHandler.sendMessage( pc, GPTradeProperty.instance.messageHandler.msgErrorSellDisabled );
      ev.setCancelled( true );
      ev.getBlock().breakNaturally();
      return;
    }

    String cType = signClaim.parent == null ? "claim" : "subclaim";
    String typeDisplay = signClaim.parent == null ?
        GPTradeProperty.instance.messageHandler.keywordClaim :
        GPTradeProperty.instance.messageHandler.keywordSubclaim;

    //TODO: Permissions?
//    if ( !GPTradeProperty.perms.has( pc, 'gptp.' + type + '.sell' ) ) {
//      MessageHandler.sendMessage( pc, GPTradeProperty.instance.messageHandler.msgErrorNoPermission );
//      ev.setCancelled( true );
//      ev.getBlock().breakNaturally();
//      return;
//    }

    int price;
    try {
      price = getValueInt( ev, 1, GPTradeProperty.instance.configHandler.cfgPricePerBlock * signClaim.getArea() );
    }
    catch ( NumberFormatException e ) {
      MessageHandler.sendMessage( pc, GPTradeProperty.instance.messageHandler.msgErrorInvalidNumber, lines.get( 1 ) );
      ev.setCancelled( true );
      ev.getBlock().breakNaturally();
      return;
    }

    if ( price <= 0 ) {
      MessageHandler.sendMessage( pc, GPTradeProperty.instance.messageHandler.msgErrorNegativePrice, lines.get( 1 ) );
      ev.setCancelled( true );
      ev.getBlock().breakNaturally();
      return;
    }

    if ( signClaim.isAdminClaim() ) {
      if ( !pc.isOp() ) {
        MessageHandler.sendMessage( pc, GPTradeProperty.instance.messageHandler.msgErrorSellAdminClaim, typeDisplay );
        ev.setCancelled( true );
        ev.getBlock().breakNaturally();
        return;
      }
    }
    else if ( cType.equals( "claim" ) && !pc.getUniqueId().equals( signClaim.ownerID ) ) {
      MessageHandler.sendMessage( pc, GPTradeProperty.instance.messageHandler.msgErrorSellNotOwner, typeDisplay );
      ev.setCancelled( true );
      ev.getBlock().breakNaturally();
      return;
    }

    ev.setCancelled( true );
    GPTradeProperty.tradeData.sell( signClaim, signClaim.isAdminClaim() ? null : pc, price, loc );
  }

  /**
   * getValueDouble - returns a formatted double value from the sign event
   */
  private int getValueInt( SignChangeEvent ev, int line, int defaultValue ) throws NumberFormatException {
    String text = PlainTextComponentSerializer.plainText().serialize( Objects.requireNonNull( ev.line( line ) ) );
    Log.info( "Sign Text: " + text );

    if ( text.isEmpty() ) {
      return defaultValue;
    }

    return Integer.parseInt( text );
  }

  /**
   * onPlayerInteract event
   */
  @EventHandler
  public void onPlayerInteract( PlayerInteractEvent ev ) {

  }

  /**
   * onBlockBreak event
   */
  @EventHandler
  public void onBlockBreak( BlockBreakEvent ev ) {

  }
}
