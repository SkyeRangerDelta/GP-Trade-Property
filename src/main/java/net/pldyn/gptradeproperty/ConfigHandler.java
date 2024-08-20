package net.pldyn.gptradeproperty;

import net.pldyn.gptradeproperty.AnnotationConfig.AnnotationConfig;
import net.pldyn.gptradeproperty.AnnotationConfig.ConfigField;
import net.pldyn.gptradeproperty.AnnotationConfig.ConfigFile;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Arrays;
import java.util.List;

/**
 * Handles the configuration of the plugin
 */
@ConfigFile( header = "Consider using a YAML editor to configure this file. \nThese are the settings for the plugin. \nBackup your changes before reloading the server." )
public class ConfigHandler extends AnnotationConfig {

  public PluginDescriptionFile pdf;

  public final String configPath = GPTradeProperty.pluginDirPath + "config.yml";

  @ConfigField( name = "GPTP.Chat.Prefix", comment = "Always displayed before a chat message." )
  public String chatPrefix = "<dark_purple>[<blue>TradeProperty</blue>]</dark_purple>";

  @ConfigField( name = "GPTP.Keywords.SignHeader", comment = "Keyword for sign tops." )
  public String signHeader = "<green>[<aqua>TradeProperty</aqua>]";



  @ConfigField( name = "GPTP.Settings.EnableSell", comment = "Allow the sale of claims?" )
  public boolean cfgEnableSell = true;



  @ConfigField( name = "GPTP.Settings.PricePerBlock", comment = "The price per block of a claim." )
  public int cfgPricePerBlock = 5;



  @ConfigField( name = "GPTP.Keywords.Sell", comment = "All possible sell sign headers" )
  public List<String> cfgSellSigns = Arrays.asList( "[sell]", "[sell claim]", "[sc]", "[sp]", "[sell property]", "[tradeproperty]", "[tp]" );

  @ConfigField( name = "GPTP.Keywords.Confirmed.Sell", comment = "The text displayed on a sell sign." )
  public String cfgDisplayConfirmed = "FOR SALE";

  @ConfigField( name = "GPTP.Keywords.Confirmed.UnderContract", comment = "[UNUSED/TODO] The text displayed on a sell sign when the property is bought but not in possession of new owner." )
  public String cfgDisplayContracted = "UNDER CONTRACT";



  @ConfigField( name = "GPTP.Rules.DisplayItemSuffix", comment = "Whether signs should show a item suffix." )
  public boolean cfgShowCurrencyAsSuffix = true;

  @ConfigField( name = "GPTP.Rules.Suffix", comment = "The suffix to use if the above is true." )
  public String cfgCurrencySymbol = "d";

  @ConfigField( name = "GPTP.Rules.UseCurrencySymbol", comment = "Allow the use of currency symbols." )
  public boolean cfgUseCurrencySymbol = true;

  @ConfigField( name = "GPTP.Rules.AcceptedTradeItem", comment = "The accepted item for trade costs." )
  public String cfgAcceptedCostItemType = "diamond";



  @ConfigField( name = "GPTP.Rules.TransferClaimBlocks", comment = "Whether claims blocks should be transferred to the new owner or not." )
  public boolean cfgTransferClaimBlocks = true;



  @ConfigField( name = "GPTP.Messaging.MessageOwner", comment = "Whether the owner is messaged when a claim is bought." )
  public boolean cfgMessageOwner = true;

  @ConfigField( name = "GPTP.Messaging.MessageBuyer", comment = "Whether the buyer is messaged when a claim is bought." )
  public boolean cfgMessageBuyer = true;

  @ConfigField( name = "GPTP.Messaging.BroadcastSell", comment = "Whether a message is broadcasted when a claim is listed for sale." )
  public boolean cfgBroadcastSell = true;



//  public ConfigHandler() {
//    this.pdf = GPTradeProperty.instance.getDescription();
//  }

  public String getString(List<String> pathData) {
    return String.join(";", pathData);
  }

  public List<String> getList(String path) {
    return Arrays.asList( path.split( ";" ) );
  }

  List<String> getConfigList( YamlConfiguration config, String path, List<String> defaultValues ) {
    config.addDefault( path, defaultValues );
    List<String> list = config.getStringList( path );
    list.replaceAll( String::toLowerCase );
    return list;
  }

  @Override
  public void loadConfig() {
    this.loadConfig( this.configPath );
  }
}
