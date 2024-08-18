package net.pldyn.gptradeproperty;

import me.EtienneDx.AnnotationConfig.AnnotationConfig;
import me.EtienneDx.AnnotationConfig.ConfigField;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Arrays;
import java.util.List;

/**
 * Handles the configuration of the plugin
 */
public class ConfigHandler extends AnnotationConfig {

  public PluginDescriptionFile pdf;

  public final String configPath = GPTradeProperty.pluginDirPath + "config.yml";

  @ConfigField( name = "GPTP.Chat.Prefix", comment = "Always displayed before a chat message." )
  public String chatPrefix = "&5[&9GP-TradeProperty&5] &f";

  @ConfigField( name = "GPTP.Keywords.SignHeader", comment = "Keyword for sign tops." )
  public String signHeader = "&6[TradeProperty]";



  @ConfigField( name = "GPTP.Settings.EnableSell", comment = "Allow the sale of claims?" )
  public boolean cfgEnableSell = true;



  @ConfigField( name = "GPTP.Settings.PricePerBlock", comment = "The price per block of a claim." )
  public int cfgPricePerBlock = 5;



  @ConfigField( name = "GPTP.Keywords.Sell", comment = "All possible sell sign headers" )
  public List<String> cfgSellSigns = Arrays.asList( "[sell]", "[sell claim]", "[sc]", "[sp]", "[sell property]", "[tradeproperty]" );

  @ConfigField( name = "GPTP.Keywords.Confirmed.Sell", comment = "The text displayed on a sell sign." )
  public String cfgDisplayConfirmed = "FOR SALE";

  @ConfigField( name = "GPTP.Keywords.Confirmed.UnderContract", comment = "[UNUSED/TODO] The text displayed on a sell sign when the property is bought but not in possession of new owner." )
  public String cfgDisplayContracted = "UNDER CONTRACT";



  @ConfigField( name = "GPTP.Rules.DisplayItemSuffix", comment = "Whether signs should show a item suffix." )
  public boolean cfgShowCurrencySuffix = true;

  @ConfigField( name = "GPTP.Rules.Suffix", comment = "The suffix to use if the above is true." )
  public String cfgCurrencySymbol = "d";

  @ConfigField(name="RealEstate.Rules.UseCurrencySymbol", comment = "Allow the use of currency symbols.")
  public boolean cfgUseCurrencySymbol = true;

  @ConfigField( name = "GPTP.Rules.AcceptedTradeItems", comment = "The accepted items for trade costs." )
  public List<String> cfgAcceptedCostItems = Arrays.asList( "minecraft:diamond" );



  @ConfigField( name = "GPTP.Rules.TransferClaimBlocks", comment = "Whether claims blocks should be transferred to the new owner or not." )
  public boolean cfgTransferClaimBlocks = true;



  @ConfigField(name="RealEstate.Messaging.MessageOwner", comment = "Whether the owner is messaged when a claim is bought.")
  public boolean cfgMessageOwner = true;

  @ConfigField(name="RealEstate.Messaging.MessageBuyer", comment = "Whether the buyer is messaged when a claim is bought.")
  public boolean cfgMessageBuyer = true;

  @ConfigField(name="RealEstate.Messaging.BroadcastSell", comment = "Whether a message is broadcasted when a claim is listed for sale.")
  public boolean cfgBroadcastSell = true;



  @ConfigField(name="RealEstate.Settings.ListPageSize", comment = "How many listings per page should appear in '/tp list'.")
  public boolean cfgListPageSize = true;



  public ConfigHandler() {
    this.pdf = GPTradeProperty.instance.getDescription();
  }

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
