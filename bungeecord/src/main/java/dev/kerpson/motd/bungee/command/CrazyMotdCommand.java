package dev.kerpson.motd.bungee.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.kerpson.motd.bungee.BungeeMotd;
import eu.crazycast.motd.shared.configuration.Configuration;
import eu.crazycast.motd.shared.util.ChatUtil;
import eu.okaeri.configs.exception.OkaeriException;
import java.util.Optional;
import java.util.stream.Collectors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.md_5.bungee.api.CommandSender;

@Command(
    name = "crazymotd",
    aliases = {"crazyMotd", "motd"}
)
@Permission("crazymotd.cmd")
public class CrazyMotdCommand {

  private final BungeeMotd plugin;
  private final BungeeAudiences adventure;
  private final Configuration configuration;

  public CrazyMotdCommand(BungeeMotd plugin, BungeeAudiences adventure, Configuration configuration) {
    this.plugin = plugin;
    this.adventure = adventure;
    this.configuration = configuration;
  }

  @Async
  @Execute
  public void defaultCommand(@Context CommandSender commandSender, @Arg Optional<String> args) {
    this.adventure.sender(commandSender).sendMessage(Component.join(
        JoinConfiguration.separator(Component.newline()),
        this.configuration.getHelpCommand().stream()
            .map(ChatUtil::deserialize)
            .collect(Collectors.toList()))
    );
  }

  @Execute(name = "reload")
  public void reloadCommand(@Context CommandSender commandSender, @Arg Optional<String> args) {
    Audience audience = this.adventure.sender(commandSender);
    try {
      this.plugin.reloadConfiguration();
      audience.sendMessage(ChatUtil.deserialize(this.configuration.getConfigurationReloadSuccessMessage()));
    } catch (OkaeriException exception) {
      exception.printStackTrace();
      audience.sendMessage(ChatUtil.deserialize(this.configuration.getConfigurationReloadFailMessage()));
    }
  }
}