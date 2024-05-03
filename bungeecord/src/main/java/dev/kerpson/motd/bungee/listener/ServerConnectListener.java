package dev.kerpson.motd.bungee.listener;

import eu.crazycast.motd.shared.notifier.PluginUpdater;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {

  private final BungeeAudiences adventure;
  private final PluginUpdater pluginUpdater;

  public ServerConnectListener(BungeeAudiences adventure, PluginUpdater pluginUpdater) {
    this.adventure = adventure;
    this.pluginUpdater = pluginUpdater;
  }

  @EventHandler
  public void onConnect(ServerConnectedEvent event) {
    ProxiedPlayer player = event.getPlayer();
    if (!player.hasPermission("crazymotd.update_notify")) {
      return;
    }

    this.pluginUpdater.notify(this.adventure.player(player));
  }
}
