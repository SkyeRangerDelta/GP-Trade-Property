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

    // Set keywords
    List<String> keywords = new ArrayList<>();
    keywords.add( "[TP]" );
    keywords.add( "[TradeProperty]" );


    // Check if this is a trade sign
    List<String> lines = new ArrayList<>();
    for ( int i = 0; i < 4; i++ ) {
      lines.add( PlainTextComponentSerializer.plainText().serialize( Objects.requireNonNull( ev.line( i ) ) ) );
    }

    Log.info( "Sign Text: " + lines );

    // Return if not a trade sign
    if ( !keywords.contains( lines.getFirst() ) ) return;

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

    double price;
    try {
      price = getValueDouble( ev );
    }
    catch ( NumberFormatException e ) {
      Log.warning( "Invalid price on sign" );
      return;
    }
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

  /**
   * getValueDouble - returns a formatted double value from the sign event
   */
  private double getValueDouble( SignChangeEvent ev ) throws NumberFormatException {
    String text = PlainTextComponentSerializer.plainText().serialize( Objects.requireNonNull( ev.line( 1 ) ) );
    Log.info( "Sign Text: " + text );
    return !text.isEmpty() ? Double.parseDouble( text ) : 0.0;
  }
}
