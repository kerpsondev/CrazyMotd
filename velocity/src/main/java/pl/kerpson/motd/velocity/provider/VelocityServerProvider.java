package pl.kerpson.motd.velocity.provider;

import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.*;
import pl.kerpson.motd.shared.provider.ServerProvider;

public class VelocityServerProvider implements ServerProvider {

  private final ProxyServer proxyServer;

  private PluginDescription pluginDescription;

  public VelocityServerProvider(ProxyServer proxyServer) {
    this.proxyServer = proxyServer;
    PluginManager pluginManager = proxyServer.getPluginManager();
    pluginManager.getPlugin("crazymotd").ifPresent(plugin -> this.pluginDescription = plugin.getDescription());
  }

  @Override
  public void shutdown() {
    this.proxyServer.shutdown();
  }

  @Override
  public String version() {
    return this.pluginDescription.getVersion().orElse("Unknown");
  }

  @Override
  public int getPlayers() {
    return this.proxyServer.getPlayerCount();
  }

  @Override
  public int getSlots() {
    return this.proxyServer.getConfiguration().getShowMaxPlayers();
  }
}