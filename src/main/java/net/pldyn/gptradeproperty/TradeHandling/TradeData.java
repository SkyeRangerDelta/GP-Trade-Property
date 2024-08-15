package net.pldyn.gptradeproperty.TradeHandling;

import net.pldyn.gptradeproperty.GPTradeProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TradeData {
  public final String tradeDataPath = GPTradeProperty.pluginDirPath + "trades.data";
  DateFormat df = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
  Date date = new Date();

  public HashMap<String, ClaimSell> claimSell;
}
