package net.pldyn.gptradeproperty;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;

public class PlayerMessaging  implements Runnable {
  private final CommandSender pc;
  private final TextComponent message;

  public PlayerMessaging( CommandSender pc, TextComponent message ) {
    this.pc = pc;
    this.message = message;
  }

  @Override
  public void run() {
    if ( message == null ) return;

    if ( pc == null ) return;

    pc.sendMessage( message );
  }
}
