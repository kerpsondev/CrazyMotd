package pl.kerpson.motd.bungee.factory;

import pl.kerpson.motd.bungee.BungeeMotd;
import pl.kerpson.motd.shared.factory.ListenerFactory;
import pl.kerpson.motd.shared.provider.LoggerProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeListenerFactory extends ListenerFactory<Listener> {

  private final BungeeMotd plugin;
  private final ProxyServer proxyServer;

  public BungeeListenerFactory(LoggerProvider loggerProvider, BungeeMotd plugin, ProxyServer proxyServer) {
    super(loggerProvider);
    this.plugin = plugin;
    this.proxyServer = proxyServer;
  }

  @Override
  public void register() {
    PluginManager pluginManager = this.proxyServer.getPluginManager();
    this.listeners.forEach(listener -> pluginManager.registerListener(this.plugin, listener));
  }

  @Override
  public void unregister() {
    PluginManager pluginManager = this.proxyServer.getPluginManager();
    pluginManager.unregisterListeners(this.plugin);
  }
}