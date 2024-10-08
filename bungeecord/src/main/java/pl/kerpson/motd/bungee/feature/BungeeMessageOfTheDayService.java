package pl.kerpson.motd.bungee.feature;

import javax.swing.text.html.Option;
import pl.kerpson.motd.shared.configuration.Configuration;
import pl.kerpson.motd.shared.configuration.section.MessageOfTheDayConfiguration;
import pl.kerpson.motd.shared.configuration.section.PlayerListConfiguration;
import pl.kerpson.motd.shared.configuration.section.VersionConfiguration;
import pl.kerpson.motd.shared.feature.MessageOfTheDay;
import pl.kerpson.motd.shared.feature.MessageOfTheDayService;
import pl.kerpson.motd.shared.feature.randomize.MessageOfTheDayRandomize;
import pl.kerpson.motd.shared.placeholer.PluginPlaceholders;
import pl.kerpson.motd.shared.util.ChatUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;

public class BungeeMessageOfTheDayService extends MessageOfTheDayService<ProxyPingEvent, BaseComponent, Players, Protocol> {

  private final static BungeeComponentSerializer BUNGEE_COMPONENT_SERIALIZER = BungeeComponentSerializer.legacy();

  public BungeeMessageOfTheDayService(Configuration configuration, PluginPlaceholders pluginPlaceholders) {
    super(configuration, pluginPlaceholders);
  }

  @Override
  protected Optional<BaseComponent> buildDescription() {
    MessageOfTheDayConfiguration messageOfTheDayConfiguration = this.configuration.getMessageOfTheDayConfiguration();
    if (!messageOfTheDayConfiguration.isEnable()) {
      return Optional.empty();
    }

    List<MessageOfTheDay> messageOfTheDays = new ArrayList<>(messageOfTheDayConfiguration.getMessageOfTheDay());
    if (messageOfTheDays.isEmpty()) {
      return Optional.of(new TextComponent(BUNGEE_COMPONENT_SERIALIZER.serialize(Component.empty())));
    }

    MessageOfTheDayRandomize messageOfTheDayRandomize = this.getRandomize();
    Optional<MessageOfTheDay> messageOfTheDayOptional = messageOfTheDayRandomize.get();
    if (!messageOfTheDayOptional.isPresent()) {
      return Optional.of(new TextComponent(BUNGEE_COMPONENT_SERIALIZER.serialize(Component.empty())));
    }

    MessageOfTheDay messageOfTheDay = messageOfTheDayOptional.get();
    return Optional.of(new TextComponent(BUNGEE_COMPONENT_SERIALIZER.serialize(Component.join(
        JoinConfiguration.separator(Component.newline()),
        messageOfTheDay.getLines().stream()
            .map(this.pluginPlaceholders::append)
            .map(ChatUtil::deserialize)
            .collect(Collectors.toList()))
    )));
  }

  @Override
  protected Optional<Players> buildPlayers() {
    PlayerListConfiguration playerListConfiguration = this.configuration.getPlayerListConfiguration();
    if (!playerListConfiguration.isEnable()) {
      return Optional.empty();
    }

    return Optional.of(new Players(
        -1,
        -1,
        playerListConfiguration.getPlayerList().stream()
            .map(this.pluginPlaceholders::append)
            .map(ChatUtil::deserialize)
            .map(ChatUtil::serializeSection)
            .map(name -> new PlayerInfo(name, UUID.randomUUID()))
            .toArray(PlayerInfo[]::new)));
  }

  @Override
  protected Optional<Protocol> buildVersion() {
    VersionConfiguration versionConfiguration = this.configuration.getVersionConfiguration();
    if (!versionConfiguration.isEnable()) {
      return Optional.empty();
    }

    String text = this.pluginPlaceholders.append(versionConfiguration.getText());
    return Optional.of(new Protocol(ChatUtil.serializeSection(ChatUtil.deserialize(text)), -1));
  }

  @Override
  public void handle(ProxyPingEvent event) {
    ServerPing serverPing = event.getResponse();
    this.description.ifPresent(serverPing::setDescriptionComponent);
    this.players.ifPresent(serverPing::setPlayers);
    this.version.ifPresent(serverPing::setVersion);

    event.setResponse(serverPing);
  }
}