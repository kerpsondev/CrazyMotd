package dev.kerpson.motd.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import eu.crazycast.motd.shared.configuration.Configuration;
import eu.crazycast.motd.shared.util.ChatUtil;
import dev.kerpson.motd.velocity.VelocityMotd;
import eu.okaeri.configs.exception.OkaeriException;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;

@Command(
    name = "crazymotd",
    aliases = {"crazyMotd", "motd"}
)
@Permission("crazymotd.cmd")
public class CrazyMotdCommand {

  private final VelocityMotd plugin;
  private final Configuration configuration;

  public CrazyMotdCommand(VelocityMotd plugin, Configuration configuration) {
    this.plugin = plugin;
    this.configuration = configuration;
  }

  @Async
  @Execute
  public void defaultCommand(@Context CommandSource commandSource) {
    commandSource.sendMessage(Component.join(
        JoinConfiguration.separator(Component.newline()),
        this.configuration.getHelpCommand().stream()
            .map(ChatUtil::deserialize)
            .collect(Collectors.toList()))
    );
  }

  @Execute(name = "reload")
  public void reloadCommand(@Context CommandSource commandSource) {
    try {
      this.plugin.reloadConfiguration();
      commandSource.sendMessage(ChatUtil.deserialize(this.configuration.getConfigurationReloadSuccessMessage()));
    } catch (OkaeriException exception) {
      exception.printStackTrace();
      commandSource.sendMessage(ChatUtil.deserialize(this.configuration.getConfigurationReloadFailMessage()));
    }
  }
}