package net.pldyn.gptradeproperty;

import me.EtienneDx.AnnotationConfig.AnnotationConfig;
import me.EtienneDx.AnnotationConfig.ConfigField;
import me.EtienneDx.AnnotationConfig.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.regex.Matcher;

@ConfigFile( header = "Consider using a YAML editor to configure this file. \nThese are the settings for various " +
    "messages dispatched by the plugin. \nBackup your changes before reloading the server. " +
    "\nUse {0} and {1} to reference comment values." )
public class MessageHandler extends AnnotationConfig {
  public PluginDescriptionFile pdf;

  @ConfigField( name = "GPTP.Keywords.TheServer" )
  public String keywordTheServer = "The server";

  @ConfigField( name = "GPTP.Keywords.Claim" )
  public String keywordClaim = "claim";

  @ConfigField( name = "GPTP.Keywords.AdminClaimPrefix" )
  public String keywordAdminClaimPrefix = "an admin";

  @ConfigField( name = "GPTP.Keywords.ClaimPrefix" )
  public String keywordClaimPrefix = "a";

  @ConfigField( name = "GPTP.Keywords.Subclaim" )
  public String keywordSubclaim = "subclaim";

  @ConfigField( name = "GPTP.Info.Claim.Info.Sell.Header", comment = "The header for the claim info sell message" )
  public String msgClaimInfoSellHeader = "$a-----= $5[$9Property Sale Info$5]$a =-----";

  @ConfigField( name = "GPTP.Info.Claim.Info.Sell.General", comment = "0: formatted price" )
  public String msgClaimInfoSellGeneral = "$2This property is for sale for {0}";

  @ConfigField( name = "GPTP.Info.Claim.Info.Sell.Header", comment = "0: claim area, 1: location, 2: formatted price" )
  public String msgClaimInfoSellSingleLine = "$2{0} $bblocks to $2Sell $bat $2{1} $bfor $a{2}";

  @ConfigField( name = "GPTP.Info.Claim.Info.Owner", comment = "0: owner name")
  public String msgClaimInfoOwner = "$bThe current owner is $a{0}";

  @ConfigField( name = "GPTP.Info.Claim.Created.Sell", comment = "0: claim prefix, 1: claim type, 2: formatted price")
  public String msgClaimCreatedSell = "$bYou have successfully created {0} {1} sale for $a{2}";

  @ConfigField( name = "GPTP.Info.Claim.Created.SellBroadcast", comment = "0: player name, 1: claim prefix, 2: claim type, 3: formatted price")
  public String msgClaimCreatedSellBroadcast = "$a{0} $bhas created {1} {2} sale for $a{3}";

  @ConfigField( name = "GPTP.Info.ClaimBuyerSold", comment = "0: claim type, 1: price" )
  public String msgClaimBuyerSold = "$bYou have purchased the {0} for $a{1}";

  @ConfigField( name = "GPTP.Info.ClaimOwnerSold", comment = "0: buyer name, 1: claim type, 2: price, 3: location" )
  public String msgClaimOwnerSold = "$a{0}$b has purchased a $a{1}$b at $a{3}$b for $a{2}";

  @ConfigField( name = "GPTP.Info.Claim.Info.MainOwner", comment = "0: owner name" )
  public String msgClaimInfoMainOwner = "$bThe main claim's owner is $a{0}";

  @ConfigField( name = "GPTP.Info.Claim.Info.SubclaimNote", comment = "0: owner name" )
  public String msgInfoClaimNote = "$bYou will only gain access to this subclaim.";



  @ConfigField( name = "GPTP.Errors.Trade.NoMoneySelf" )
  public String msgErrorNoMoneySelf = "$cYou do not have enough to buy this property.";

  @ConfigField( name = "GPTP.Errors.Trade.NoMoneyOther" )
  public String msgErrorNoMoneyOther = "$c{0} does not have enough to buy this property.";

  @ConfigField( name = "GPTP.Errors.NoWithdrawSelf" )
  public String msgErrorNoWithdrawSelf = "$cCould not withdraw the money!";

  @ConfigField( name = "GPTP.Errors.NoWithdrawOther", comment = "0: Other player" )
  public String msgErrorNoWithdrawOther = "$cCould not withdraw the money from {0}!";

  @ConfigField( name = "GPTP.Errors.NoDepositSelf", comment = "0: Other player" )
  public String msgErrorNoDepositSelf = "$cCould not deposit the money to you, refunding {0}!";

  @ConfigField( name = "GPTP.Errors.NoDepositOther", comment = "0: Other player" )
  public String msgErrorNoDepositOther = "$cCould not deposit the money to {0}, refunding you!";

  @ConfigField( name = "GPTP.Errors.Sign.OngoingTransaction" )
  public String msgErrorSignOngoingTrade = "$cThis claim already has an ongoing transaction!";

  @ConfigField( name = "GPTP.Errors.Sign.ParentOngoingTransaction" )
  public String msgErrorSignOngoingTradeParent = "$cThis claim's parent already has an ongoing transaction!";

  @ConfigField( name = "GPTP.Errors.Sign.SubclaimOngoingTransaction" )
  public String msgErrorSignOngoingTradeSubclaim = "$cThis claim has subclaims with ongoing transactions!";

  @ConfigField( name = "GPTP.Errors.SellDisabled" )
  public String msgErrorSellDisabled = "$cSelling claims is disabled on this server!";

  @ConfigField( name = "GPTP.Errors.InvalidNumber", comment = "Message for wrong price data. 0: the price" )
  public String msgErrorInvalidNumber = "$c{0} is not a valid number!";

  @ConfigField( name = "GPTP.Errors.NegativePrice", comment = "0: price" )
  public String msgErrorNegativePrice = "$cThe price must be greater than zero!";

  @ConfigField( name = "GPTP.Errors.SellAdminClaimError", comment = "0: claim type" )
  public String msgErrorSellAdminClaim = "$cYou don't have permission to sell this admin {0}";

  @ConfigField( name = "GPTP.Errors.NotOwnerError", comment = "0: claim type" )
  public String msgErrorSellNotOwner = "$cYou can only sell your own {0}";

  @ConfigField( name = "GPTP.Errors.NoTransaction" )
  public String msgErrorNoTrade = "$cThis claim is no longer on the market.";

  @ConfigField( name = "GPTP.Errors.SignNotAuthor" )
  public String msgErrorSignNotAuthor = "$cYou are not the author of this sign!";

  @ConfigField( name = "GPTP.Errors.SignNotAdmin" )
  public String msgErrorSignNotAdmin = "$cOnly admins can destroy this sign!";

  @ConfigField( name = "GPTP.Errors.ClaimAlreadyOwner", comment = "0: claim type" )
  public String msgErrorClaimAlreadyOwner = "$cYou are already the owner of this ${0}";

  @ConfigField( name = "GPTP.Errors.ClaimNotSoldByOwner", comment = "0: claim type" )
  public String msgErrorClaimNotSoldByOwner = "$cThis ${0} is not sold by it's owner!";

  @ConfigField( name = "GPTP.Errors.ClaimNotExists" )
  public String msgErrorClaimNonExistent = "$cThis claim doesn't exist!";

  @ConfigField( name = "GPTP.Errors.NoClaimBlocks", comment = "0: claim area, 1: claim blocks remaining, 2: missing blocks" )
  public String msgErrorNoClaimBlocks = "$cYou need $a{2}$c more claim blocks to buy this area! (Requires $a{0}$c blocks and you have $a{1}$c)";

  @ConfigField( name = "GPTP.Errors.UnexpectedError" )
  public String msgErrorUnexpected = "$cSomething unexpected has occurred. Please contact an admin.";


  public MessageHandler() {
    this.pdf = GPTradeProperty.instance.getDescription();
  }

  public static String getMessage( String template, String... args ) {
    return getMessage( template, true, args );
  }

  public static String getMessage( String template, boolean withPrefix, String... args ) {
    if ( withPrefix ) {
      template = GPTradeProperty.instance.configHandler.chatPrefix + template;
    }

    template = ChatColor.translateAlternateColorCodes( '$', template );

    for ( int i = 0; i < args.length; i++ ) {
      String param = args[i];
      template = template.replaceAll( "\\{" + i + "}", Matcher.quoteReplacement( param ) );
    }

    return template;
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String msgTemplate, String... args )  {
    sendMessage( player, msgTemplate, 0, args );
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String msgTemplate, long delayInTicks, String... args ) {
    String message = getMessage( msgTemplate, args );
    sendMessage( player, message, delayInTicks );
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String message ) {
    sendMessage(player, getMessage( message ), 0);
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String message, Boolean fixColors ) {
    sendMessage(player, fixColors ? getMessage( message ) : message, 0);
  }

  public static void sendMessage( CommandSender player, String message, long delayInTicks ) {
    PlayerMessaging task = new PlayerMessaging(player, message);

    if ( delayInTicks > 0 ) {
      GPTradeProperty.instance.getServer().getScheduler().runTaskLater(GPTradeProperty.instance, task, delayInTicks);
    } else {
      task.run();
    }
  }
}
