package net.pldyn.gptradeproperty.TradeHandling;

import me.ryanhamshire.GriefPrevention.Claim;
import net.pldyn.gptradeproperty.GPTradeProperty;
import net.pldyn.gptradeproperty.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ClaimSell extends TradeTransaction {
  public ClaimSell( Claim claim, Player pc, double price, Location sign ) {
    super( claim, pc, price, sign );
  }

  public ClaimSell( Map<String, Object> map ) {
    super( map );
  }

  @Override
  public boolean update() {

    if ( sign.getBlock().getState() instanceof Sign ) {
      Sign s = ( Sign ) sign.getBlock().getState();

      //TODO: Refactor to account for multiple sides
      s.setLine( 0, MessageHandler.getMessage( GPTradeProperty.instance.configHandler.signHeader ) );
      s.setLine( 1, MessageHandler.getMessage( GPTradeProperty.instance.configHandler.cfgDisplayConfirmed ) );
      s.setLine( 2, owner != null ? Utils.getSignString( Bukkit.getOfflinePlayer( owner ).getName) : "SERVER" );
    }
    else {

    }

    return false;
  }

  @Override
  public boolean tryCancelTrade( Player pc, boolean force ) {
    return false;
  }

  @Override
  public void interact( Player player ) {

  }

  @Override
  public void preview( Player player ) {

  }

  @Override
  public void setOwner( UUID owner ) {

  }

  @Override
  public void messageData( CommandSender cs ) {

  }
}
