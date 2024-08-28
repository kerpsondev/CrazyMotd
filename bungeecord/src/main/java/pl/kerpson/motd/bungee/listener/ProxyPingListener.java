package pl.kerpson.motd.bungee.listener;

import pl.kerpson.motd.bungee.feature.BungeeMessageOfTheDayService;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener implements Listener {

  private final BungeeMessageOfTheDayService messageOfTheDayService;

  public ProxyPingListener(BungeeMessageOfTheDayService messageOfTheDayService) {
    this.messageOfTheDayService = messageOfTheDayService;
  }

  @EventHandler
  public void onPing(ProxyPingEvent event) {
    this.messageOfTheDayService.handle(event);
  }
}