package net.pldyn.gptradeproperty.TradeHandling;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.pldyn.gptradeproperty.GPTradeProperty;
import net.pldyn.gptradeproperty.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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

  public HashMap<String, ClaimSell> claimSell;

  public TradeData() {
    loadData();
  }

  public void loadData() {
    claimSell = new HashMap<>();
    File f = new File( this.tradeDataPath );
    if ( f.exists() ) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration( f );

      ConfigurationSection sellSection = config.getConfigurationSection( "Sell" );

      if ( sellSection != null ) {
        for ( String key : sellSection.getKeys( false ) ) {
          ClaimSell cs = ( ClaimSell ) sellSection.get( key );
          claimSell.put( key, cs );
        }
      }
    }
  }

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
      MessageHandler.sendMessage( ( CommandSender ) player, GPTradeProperty.instance.messageHandler.msgClaimCreatedSell,
          claimPrefix, claimTypeDisplay, "" + price );
    }

    if ( GPTradeProperty.instance.configHandler.cfgBroadcastSell ) {
      for ( Player p : Bukkit.getServer().getOnlinePlayers() ) {
        if ( p != player ) {
          MessageHandler.sendMessage( ( CommandSender ) p, GPTradeProperty.instance.messageHandler.msgClaimCreatedSellBroadcast,
            player == null ?
              GPTradeProperty.instance.messageHandler.keywordTheServer :
              PlainTextComponentSerializer.plainText().serialize( player.displayName() ),
            claimPrefix,
            claimTypeDisplay, "" + price );
        }
      }
    }
  }

  public Trade getTrade( Player player ) {
    if ( player == null ) return null;

    Claim c = GriefPrevention.instance.dataStore.getClaimAt( player.getLocation(), true, null );
    return getTransaction( c );
  }
}
