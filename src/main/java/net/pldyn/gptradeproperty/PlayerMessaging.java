package net.pldyn.gptradeproperty;

import org.bukkit.command.CommandSender;

public class PlayerMessaging  implements Runnable {
  private CommandSender pc;
  private String message;

  public PlayerMessaging( CommandSender pc, String message ) {
    this.pc = pc;
    this.message = message;
  }

  @Override
  public void run() {
    if ( message == null || message.isEmpty() ) return;

    if ( pc == null ) return;

    pc.sendMessage( message );
  }
}
