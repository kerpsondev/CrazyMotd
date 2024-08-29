package pl.kerpson.motd.shared.feature;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import pl.kerpson.motd.shared.configuration.Configuration;
import pl.kerpson.motd.shared.configuration.section.MessageOfTheDayConfiguration.Type;
import pl.kerpson.motd.shared.feature.randomize.MessageOfTheDayRandomize;
import pl.kerpson.motd.shared.feature.randomize.QueueMessageOfTheDayRandomize;
import pl.kerpson.motd.shared.feature.randomize.RandomMessageOfTheDayRandomize;
import pl.kerpson.motd.shared.placeholer.PluginPlaceholders;
import java.util.Optional;
import java.util.function.IntFunction;

public abstract class MessageOfTheDayService<E, D, P, V> {

  protected final Configuration configuration;
  protected final PluginPlaceholders pluginPlaceholders;

  protected Optional<P> players;
  protected Optional<V> version;
  protected Optional<D> description;

  protected int descriptionIndex;

  private Map<Type, MessageOfTheDayRandomize> messageOfTheDayRandomize;

  public MessageOfTheDayService(Configuration configuration, PluginPlaceholders pluginPlaceholders) {
    this.configuration = configuration;
    this.pluginPlaceholders = pluginPlaceholders;
    this.messageOfTheDayRandomize = ImmutableMap.of(
        Type.RANDOM, new RandomMessageOfTheDayRandomize(this.configuration),
        Type.QUEUE, new QueueMessageOfTheDayRandomize(this.configuration)
    );

    this.reloadRandomize();
    this.update();
  }

  protected MessageOfTheDayRandomize getRandomize() {
    return messageOfTheDayRandomize.get(this.configuration.getMessageOfTheDayConfiguration().getUpdateType());
  }

  public void reloadRandomize() {
    for (Type type : Type.values()) {
      this.messageOfTheDayRandomize.get(type).reload();
    }
  }

  public void update() {
    this.description = this.buildDescription();
    this.players = this.buildPlayers();
    this.version = this.buildVersion();
  }

  protected abstract Optional<D> buildDescription();

  protected abstract Optional<P> buildPlayers();

  protected abstract Optional<V> buildVersion();

  public abstract void handle(E event);
}