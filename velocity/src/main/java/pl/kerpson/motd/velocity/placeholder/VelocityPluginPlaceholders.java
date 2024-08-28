package pl.kerpson.motd.velocity.placeholder;

import com.velocitypowered.api.proxy.ProxyServer;
import pl.kerpson.motd.shared.configuration.Configuration;
import pl.kerpson.motd.shared.placeholer.PluginPlaceholders;
import pl.kerpson.motd.shared.provider.ServerProvider;

public class VelocityPluginPlaceholders extends PluginPlaceholders {

  private final ProxyServer proxyServer;

  public VelocityPluginPlaceholders(
      Configuration configuration,
      ServerProvider serverProvider,
      ProxyServer proxyServer
  ) {
    super(configuration, serverProvider);
    this.proxyServer = proxyServer;
  }

  @Override
  protected int getOnlineForServer(String serverName) {
    return this.proxyServer.getServer(serverName).map(server -> server.getPlayersConnected().size()).orElse(0);
  }
}
