package net.pldyn.gptradeproperty.TradeHandling;

import me.ryanhamshire.GriefPrevention.Claim;
import net.pldyn.gptradeproperty.GPTradeProperty;
import net.pldyn.gptradeproperty.MessageHandler;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TradeData {
  public final String tradeDataPath = GPTradeProperty.pluginDirPath + "trades.data";
  DateFormat df = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
  Date date = new Date();

  public HashMap<String, ClaimSell> claimSell;

  public void saveData() {
    YamlConfiguration config = new YamlConfiguration();
    for ( ClaimSell cs : claimSell.values() ) {
      config.set( "Sell." + cs.claimId, cs );
    }

    try {
      config.save( new File( this.tradeDataPath ) );
    }
    catch ( IOException e ) {
      GPTradeProperty.instance.Log.info( "Failed to save trade data!" );
    }
  }

  public boolean anyTransaction( Claim claim ) {
    return claim != null &&
        ( claimSell.containsKey( claim.getID().toString() ) );
  }

  public Trade getTransaction( Claim claim ) {
    if ( claimSell.containsKey( claim.getID().toString() ) ) {
      return claimSell.get( claim.getID().toString() );
    }
    return null;
  }

  public void cancelTransaction( Claim claim ) {
    if ( anyTransaction( claim ) ) {
      Trade tr = getTransaction( claim );
      cancelTrade( tr );
    }
    saveData();
  }

  public void cancelTrade( Trade tr ) {
    if ( tr.getHolder() != null ) {
      tr.getHolder().breakNaturally();
    }

    if ( tr instanceof ClaimSell ) {
      claimSell.remove( String.valueOf( ( (ClaimSell) tr ).claimId ) );
    }

    saveData();
  }

  public boolean canCancelTransaction ( Trade tr ) {
    return tr instanceof ClaimSell;
  }

  public void sell ( Claim claim, Player player, int price, Location sign ) {
    ClaimSell cs = new ClaimSell( claim, claim.isAdminClaim() ? null : player, price, sign );
    claimSell.put( claim.getID().toString(), cs );
    cs.update();
    saveData();

    String claimPrefix = claim.isAdminClaim() ? GPTradeProperty.instance.messageHandler.keywordAdminClaimPrefix :
        GPTradeProperty.instance.messageHandler.keywordClaimPrefix;
    String claimTypeDisplay = claim.isAdminClaim() ? GPTradeProperty.instance.messageHandler.keywordClaim :
        GPTradeProperty.instance.messageHandler.keywordSubclaim;

    if ( player != null ) {
      MessageHandler.sendMessage( player, GPTradeProperty.instance.messageHandler.msgClaimCreatedSell );
    }
  }
}
