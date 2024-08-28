package pl.kerpson.motd.velocity.factory;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.ProxyServer;
import pl.kerpson.motd.shared.factory.ListenerFactory;
import pl.kerpson.motd.shared.provider.LoggerProvider;
import pl.kerpson.motd.velocity.VelocityMotd;

public class VelocityListenerFactory extends ListenerFactory<Object> {

  private final VelocityMotd plugin;
  private final ProxyServer proxyServer;

  public VelocityListenerFactory(LoggerProvider loggerProvider, VelocityMotd plugin, ProxyServer proxyServer) {
    super(loggerProvider);
    this.plugin = plugin;
    this.proxyServer = proxyServer;
  }

  @Override
  public void register() {
    EventManager eventManager = this.proxyServer.getEventManager();
    this.listeners.forEach(listener -> eventManager.register(this.plugin, listener));
  }

  @Override
  public void unregister() {
    EventManager eventManager = this.proxyServer.getEventManager();
    eventManager.unregisterListeners(this.plugin);
  }
}