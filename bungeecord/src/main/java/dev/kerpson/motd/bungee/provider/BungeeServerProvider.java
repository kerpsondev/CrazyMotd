package dev.kerpson.motd.bungee.provider;

import eu.crazycast.motd.shared.provider.ServerProvider;
import net.md_5.bungee.api.ProxyServer;

public class BungeeServerProvider implements ServerProvider {

  private final ProxyServer proxyServer;

  public BungeeServerProvider(ProxyServer proxyServer) {
    this.proxyServer = proxyServer;
  }

  @Override
  public void shutdown() {
    this.proxyServer.stop();
  }

  @Override
  public String version() {
    return proxyServer.getVersion();
  }

  @Override
  public int getPlayers() {
    return this.proxyServer.getOnlineCount();
  }

  @Override
  public int getSlots() {
    return this.proxyServer.getConfig().getPlayerLimit();
  }
}