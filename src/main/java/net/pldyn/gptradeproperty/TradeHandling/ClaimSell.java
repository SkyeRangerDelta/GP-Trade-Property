package net.pldyn.gptradeproperty.TradeHandling;

import me.ryanhamshire.GriefPrevention.Claim;
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
