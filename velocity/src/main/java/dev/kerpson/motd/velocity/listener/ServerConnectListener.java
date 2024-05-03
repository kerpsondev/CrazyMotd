package dev.kerpson.motd.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import eu.crazycast.motd.shared.notifier.PluginUpdater;

public class ServerConnectListener {

  private final PluginUpdater pluginUpdater;

  public ServerConnectListener(PluginUpdater pluginUpdater) {
    this.pluginUpdater = pluginUpdater;
  }

  @Subscribe
  public EventTask onConnect(ServerConnectedEvent event) {
    return EventTask.async(() -> {
      Player player = event.getPlayer();
      if (event.getPreviousServer().isPresent()) {
        return;
      }

      if (!player.hasPermission("crazymotd.update_notify")) {
        return;
      }

      this.pluginUpdater.notify(player);
    });
  }
}
