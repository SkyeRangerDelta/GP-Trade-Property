package net.pldyn.gptradeproperty.TradeHandling;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Trade {
  public Block getHolder();
  public UUID getOwner();
  public void setOwner( UUID owner );
  public void interact( Player player );
  public void preview ( Player player );
  public boolean update();
  public boolean tryCancelTrade( Player pc );
  public boolean tryCancelTrade( Player pc, boolean force );
  public void messageData ( CommandSender  cs );
}
