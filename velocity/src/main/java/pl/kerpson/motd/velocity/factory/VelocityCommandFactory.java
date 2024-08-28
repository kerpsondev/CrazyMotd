package pl.kerpson.motd.velocity.factory;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.velocity.LiteVelocityFactory;
import pl.kerpson.motd.shared.factory.CommandFactory;
import pl.kerpson.motd.shared.provider.LoggerProvider;

public class VelocityCommandFactory extends CommandFactory<Object> {

  private final ProxyServer proxyServer;
  private LiteCommands<CommandSource> liteCommands;

  public VelocityCommandFactory(ProxyServer proxyServer, LoggerProvider loggerProvider) {
    super(loggerProvider);
    this.proxyServer = proxyServer;
  }

  @Override
  public void register() {
    this.liteCommands = LiteVelocityFactory.builder(this.proxyServer)
        .commands(this.commands.toArray())
        .build();
  }

  @Override
  public void unregister() {
    if (this.liteCommands != null) {
      this.liteCommands.unregister();
    }
  }
}