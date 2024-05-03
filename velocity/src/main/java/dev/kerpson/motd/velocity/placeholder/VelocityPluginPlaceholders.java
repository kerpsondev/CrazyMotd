package dev.kerpson.motd.velocity.placeholder;

import com.velocitypowered.api.proxy.ProxyServer;
import eu.crazycast.motd.shared.configuration.Configuration;
import eu.crazycast.motd.shared.placeholer.PluginPlaceholders;
import eu.crazycast.motd.shared.provider.ServerProvider;

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
