package pl.kerpson.motd.bungee.placeholder;

import pl.kerpson.motd.shared.configuration.Configuration;
import pl.kerpson.motd.shared.placeholer.PluginPlaceholders;
import pl.kerpson.motd.shared.provider.ServerProvider;
import java.util.Optional;
import net.md_5.bungee.api.ProxyServer;

public class BungeePluginPlaceholders extends PluginPlaceholders {

  private final ProxyServer proxyServer;

  public BungeePluginPlaceholders(
      Configuration configuration,
      ServerProvider serverProvider,
      ProxyServer proxyServer
  ) {
    super(configuration, serverProvider);
    this.proxyServer = proxyServer;
  }

  @Override
  protected int getOnlineForServer(String serverName) {
    return Optional.ofNullable(this.proxyServer.getServerInfo(serverName))
        .map(serverInfo -> serverInfo.getPlayers().size())
        .orElse(0);
  }
}
