package pl.kerpson.motd.shared.feature.randomize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import pl.kerpson.motd.shared.configuration.Configuration;
import pl.kerpson.motd.shared.configuration.section.MessageOfTheDayConfiguration;
import pl.kerpson.motd.shared.feature.MessageOfTheDay;

public class RandomMessageOfTheDayRandomize implements MessageOfTheDayRandomize {

  private final Configuration configuration;
  private Queue<MessageOfTheDay> messageOfTheDays;

  public RandomMessageOfTheDayRandomize(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void reload() {
    MessageOfTheDayConfiguration messageOfTheDayConfiguration = this.configuration.getMessageOfTheDayConfiguration();
    this.messageOfTheDays = new RandomQueue<>(messageOfTheDayConfiguration.getMessageOfTheDay());
  }

  @Override
  public Optional<MessageOfTheDay> get() {
    MessageOfTheDayConfiguration messageOfTheDayConfiguration = this.configuration.getMessageOfTheDayConfiguration();
    if (messageOfTheDayConfiguration.getMessageOfTheDay().isEmpty()) {
      return Optional.empty();
    }

    if (this.messageOfTheDays.size() == 1) {
      return Optional.of(this.messageOfTheDays.element());
    }

    if (this.messageOfTheDays.isEmpty()) {
      this.reload();
    }

    MessageOfTheDay messageOfTheDay = this.messageOfTheDays.poll();
    return Optional.ofNullable(messageOfTheDay);
  }

  static class RandomQueue<T> extends LinkedList<T> {

    private final static Random RANDOM = new Random();

    RandomQueue(Collection<T> collection) {
      super(collection);
    }

    @Override
    public T poll() {
      if (this.isEmpty()) {
        return null;
      }

      int randomIndex = RANDOM.nextInt(this.size());
      return remove(randomIndex);
    }
  }
}
