package net.pldyn.gptradeproperty;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.pldyn.gptradeproperty.AnnotationConfig.AnnotationConfig;
import net.pldyn.gptradeproperty.AnnotationConfig.ConfigField;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.regex.Matcher;

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
  public String msgClaimInfoSellHeader = "<green>-----= <dark_purple>[<blue>Property Sale Info<dark_purple>]<green> =-----";

  @ConfigField( name = "GPTP.Info.Claim.Info.Sell.General", comment = "0: formatted price" )
  public String msgClaimInfoSellGeneral = "<dark_green>This property is for sale for {0}";

  @ConfigField( name = "GPTP.Info.Claim.Info.Sell.Header", comment = "0: claim area, 1: location, 2: formatted price" )
  public String msgClaimInfoSellSingleLine = "<dark_green>{0} <aqua>blocks to <dark_green>Sell <aqua>at <dark_green>{1} <aqua>for <green>{2}";

  @ConfigField( name = "GPTP.Info.Claim.Info.Owner", comment = "0: owner name")
  public String msgClaimInfoOwner = "<aqua>The current owner is <green>{0}";

  @ConfigField( name = "GPTP.Info.Claim.Created.Sell", comment = "0: claim prefix, 1: claim type, 2: formatted price")
  public String msgClaimCreatedSell = "<blue>You have successfully created {0} {1} sale for <green>{2}";

  @ConfigField( name = "GPTP.Info.Claim.Created.SellBroadcast", comment = "0: player name, 1: claim prefix, 2: claim type, 3: formatted price")
  public String msgClaimCreatedSellBroadcast = "<green>{0} <aqua>has created {1} {2} sale for <green>{3}";

  @ConfigField( name = "GPTP.Info.ClaimBuyerSold", comment = "0: claim type, 1: price" )
  public String msgClaimBuyerSold = "<aqua>You have purchased the {0} for <green>{1}";

  @ConfigField( name = "GPTP.Info.ClaimOwnerSold", comment = "0: buyer name, 1: claim type, 2: price, 3: location" )
  public String msgClaimOwnerSold = "<green>{0}<aqua> has purchased a <green>{1}<aqua> at <green>{3}<aqua> for <green>{2}";

  @ConfigField( name = "GPTP.Info.Claim.Info.MainOwner", comment = "0: owner name" )
  public String msgClaimInfoMainOwner = "<aqua>The main claim's owner is <green>{0}";

  @ConfigField( name = "GPTP.Info.Claim.Info.SubclaimNote", comment = "0: owner name" )
  public String msgInfoClaimNote = "<aqua>You will only gain access to this subclaim.";



  @ConfigField( name = "GPTP.Errors.Trade.NoMoneySelf" )
  public String msgErrorNoMoneySelf = "<red>You do not have enough to buy this property.";

  @ConfigField( name = "GPTP.Errors.Trade.NoMoneyOther" )
  public String msgErrorNoMoneyOther = "<red>{0} does not have enough to buy this property.";

  @ConfigField( name = "GPTP.Errors.NoWithdrawSelf" )
  public String msgErrorNoWithdrawSelf = "<red>Could not withdraw the money!";

  @ConfigField( name = "GPTP.Errors.NoWithdrawOther", comment = "0: Other player" )
  public String msgErrorNoWithdrawOther = "<red>Could not withdraw the money from {0}!";

  @ConfigField( name = "GPTP.Errors.NoDepositSelf", comment = "0: Other player" )
  public String msgErrorNoDepositSelf = "<red>Could not deposit the money to you, refunding {0}!";

  @ConfigField( name = "GPTP.Errors.NoDepositOther", comment = "0: Other player" )
  public String msgErrorNoDepositOther = "<red>Could not deposit the money to {0}, refunding you!";

  @ConfigField( name = "GPTP.Errors.Sign.OngoingTransaction" )
  public String msgErrorSignOngoingTrade = "<red>This claim already has an ongoing transaction!";

  @ConfigField( name = "GPTP.Errors.Sign.ParentOngoingTransaction" )
  public String msgErrorSignOngoingTradeParent = "<red>This claim's parent already has an ongoing transaction!";

  @ConfigField( name = "GPTP.Errors.Sign.SubclaimOngoingTransaction" )
  public String msgErrorSignOngoingTradeSubclaim = "<red>This claim has subclaims with ongoing transactions!";

  @ConfigField( name = "GPTP.Errors.SellDisabled" )
  public String msgErrorSellDisabled = "<red>Selling claims is disabled on this server!";

  @ConfigField( name = "GPTP.Errors.InvalidNumber", comment = "Message for wrong price data. 0: the price" )
  public String msgErrorInvalidNumber = "<red>{0} is not a valid number!";

  @ConfigField( name = "GPTP.Errors.NegativePrice", comment = "0: price" )
  public String msgErrorNegativePrice = "<red>The price must be greater than zero!";

  @ConfigField( name = "GPTP.Errors.SellAdminClaimError", comment = "0: claim type" )
  public String msgErrorSellAdminClaim = "<red>You don't have permission to sell this admin {0}";

  @ConfigField( name = "GPTP.Errors.NotOwnerError", comment = "0: claim type" )
  public String msgErrorSellNotOwner = "<red>You can only sell your own {0}";

  @ConfigField( name = "GPTP.Errors.NoTransaction" )
  public String msgErrorNoTrade = "<red>This claim is no longer on the market.";

  @ConfigField( name = "GPTP.Errors.SignNotAuthor" )
  public String msgErrorSignNotAuthor = "<red>You are not the author of this sign!";

  @ConfigField( name = "GPTP.Errors.SignNotAdmin" )
  public String msgErrorSignNotAdmin = "<red>Only admins can destroy this sign!";

  @ConfigField( name = "GPTP.Errors.SignNotInClaim" )
  public String msgErrorSignNotInClaim = "<red>Property signs must be in a claim!";

  @ConfigField( name = "GPTP.Errors.ClaimAlreadyOwner", comment = "0: claim type" )
  public String msgErrorClaimAlreadyOwner = "<red>You are already the owner of this {0}";

  @ConfigField( name = "GPTP.Errors.ClaimNotSoldByOwner", comment = "0: claim type" )
  public String msgErrorClaimNotSoldByOwner = "<red>This {0} is not sold by it's owner!";

  @ConfigField( name = "GPTP.Errors.ClaimNotExists" )
  public String msgErrorClaimNonExistent = "<red>This claim doesn't exist!";

  @ConfigField( name = "GPTP.Errors.NoClaimBlocks", comment = "0: claim area, 1: claim blocks remaining, 2: missing blocks" )
  public String msgErrorNoClaimBlocks = "<red>You need <green>{2}<red> more claim blocks to buy this area! (Requires <green>{0}<red> blocks and you have <green>{1}<red>)";

  @ConfigField( name = "GPTP.Errors.UnexpectedError" )
  public String msgErrorUnexpected = "<red>Something unexpected has occurred. Please contact an admin.";


//  public MessageHandler() {
//    this.pdf = GPTradeProperty.instance.getDescription();
//  }

  public static TextComponent getMessage( String template, String... args ) {
    return getMessage( template, true, args );
  }

  public static TextComponent getMessage( String template, boolean withPrefix, String... args ) {
    TextComponent newTemplate = null;

    if ( withPrefix ) {
      template = GPTradeProperty.instance.configHandler.chatPrefix + " " + template;
    }

    for ( int i = 0; i < args.length; i++ ) {
      String param = args[i];
      template = template.replaceAll( "\\{" + i + "}", Matcher.quoteReplacement( param ) );
    }

    GPTradeProperty.instance.Log.info( "Message Template: " + template );

    MiniMessage mm = MiniMessage.miniMessage();
    newTemplate = ( TextComponent ) mm.deserialize( template );

    GPTradeProperty.instance.Log.info( "Message: " + newTemplate );

    return newTemplate;
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String msgTemplate, String... args )  {
    sendMessage( player, msgTemplate, 0, args );
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String msgTemplate, long delayInTicks, String... args ) {
    TextComponent message = getMessage( msgTemplate, args );
    sendMessage( player, message, delayInTicks );
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String message ) {
    sendMessage(player, getMessage( message ), 0);
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String message, Boolean fixColors ) {
    sendMessage(player, fixColors ? getMessage( message ) : Component.text( message ), 0);
  }

  public static void sendMessage( CommandSender player, TextComponent message, long delayInTicks ) {
    PlayerMessaging task = new PlayerMessaging(player, message);

    if ( delayInTicks > 0 ) {
      GPTradeProperty.instance.getServer().getScheduler().runTaskLater(GPTradeProperty.instance, task, delayInTicks);
    } else {
      task.run();
    }
  }
}
