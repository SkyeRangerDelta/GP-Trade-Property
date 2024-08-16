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

  @ConfigField( name = "GPTP.Info.Claim.Info.Sell.Header", comment = "The header for the claim info sell message" )
  public String msgClaimInfoSellHeader = "$a-----= $5[$9Property Sale Info$5]$a =-----";

  @ConfigField( name = "GPTP.Info.Claim.Info.Sell.General", comment = "0: formatted price" )
  public String msgClaimInfoSellGeneral = "$2This property is for sale for {0}";

  @ConfigField( name = "GPTP.Info.Claim.Info.Sell.Header", comment = "0: claim area, 1: location, 2: formatted price" )
  public String msgClaimInfoSellSingleLine = "$2{0} $bblocks to $2Sell $bat $2{1} $bfor $a{2}";

  @ConfigField( name = "GPTP.Info.Claim.Info.Owner", comment = "0: owner name")
  public String msgClaimInfoOwner = "$bThe current owner is $a{0}";

  @ConfigField( name = "RealEstate.Info.Claim.Created.Sell", comment = "0: claim prefix, 1: claim type, 2: formatted price")
  public String msgClaimCreatedSell = "$bYou have successfully created {0} {1} sale for $a{2}";

  @ConfigField( name = "RealEstate.Info.Claim.Created.SellBroadcast", comment = "0: player name, 1: claim prefix, 2: claim type, 3: formatted price")
  public String msgClaimCreatedSellBroadcast = "$a{0} $bhas created {1} {2} sale for $a{3}";



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
  public static void sendMessage( CommandSender player, String msgTemplate, String... args)  {
    sendMessage(player, msgTemplate, 0, args);
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String msgTemplate, long delayInTicks, String... args ) {
    String message = getMessage(msgTemplate, args);
    sendMessage(player, message, delayInTicks);
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String message ) {
    sendMessage(player, getMessage(message), 0);
  }

  //sends a color-coded message to a player
  public static void sendMessage( CommandSender player, String message, Boolean fixColors ) {
    sendMessage(player, fixColors ? getMessage(message) : message, 0);
  }

  public static void sendMessage( CommandSender player, String message, long delayInTicks ) {
    PlayerMessaging task = new PlayerMessaging(player, message);

    if (delayInTicks > 0) {
      GPTradeProperty.instance.getServer().getScheduler().runTaskLater(GPTradeProperty.instance, task, delayInTicks);
    } else {
      task.run();
    }
  }
}
