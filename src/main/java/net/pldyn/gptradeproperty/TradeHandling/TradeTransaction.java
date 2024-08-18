package net.pldyn.gptradeproperty.TradeHandling;

import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class TradeTransaction implements ConfigurationSerializable, Trade {

  public long claimId;
  public UUID owner = null;
  public int price;
  public Location sign = null;

  public TradeTransaction( Claim claim, Player pc, int price, Location sign ) {
    this.claimId = claim.getID();
    this.owner = pc != null ? pc.getUniqueId() : null;
    this.price = price;
    this.sign = sign;
  }

  public TradeTransaction( Map<String, Object> map ) {
    this.claimId = Long.parseLong( String.valueOf( map.get("claimId") ) );

    if ( map.get( "owner" ) != null ) {
      this.owner = UUID.fromString( String.valueOf( map.get("owner") ) );
    }

    this.price = Integer.parseInt( String.valueOf( map.get("price") ) );

    if ( map.get( "signLocation" ) != null ) {
      this.sign = (Location) map.get("signLocation");
    }
  }

  public TradeTransaction() {

  }

  @Override
  public @NotNull Map<String, Object> serialize() {
    Map<String, Object> map = new HashMap<>();

    map.put( "claimId", this.claimId );

    if ( owner != null ) {
      map.put( "owner", owner.toString() );
    }

    map.put( "price", this.price );

    if ( sign != null ) {
      map.put( "signLocation", sign );
    }

    return map;
  }

  @Override
  public Block getHolder() {
    return sign.getBlock().getState() instanceof Sign ? sign.getBlock() : null;
  }

  @Override
  public UUID getOwner() {
    return owner;
  }

  @Override
  public boolean tryCancelTrade( Player pc ) {
    return this.tryCancelTrade( pc, false );
  }
}
