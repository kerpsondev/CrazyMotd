package pl.kerpson.motd.shared.feature;

import pl.kerpson.motd.shared.configuration.Configuration;
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

  public MessageOfTheDayService(Configuration configuration, PluginPlaceholders pluginPlaceholders) {
    this.configuration = configuration;
    this.pluginPlaceholders = pluginPlaceholders;
    this.update();
  }

  protected void setDescriptionIndex(IntFunction<Integer> descriptionIndexFunction) {
    this.descriptionIndex = descriptionIndexFunction.apply(this.descriptionIndex);
  }

  protected void setDescriptionIndex(int descriptionIndex) {
    this.descriptionIndex = descriptionIndex;
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