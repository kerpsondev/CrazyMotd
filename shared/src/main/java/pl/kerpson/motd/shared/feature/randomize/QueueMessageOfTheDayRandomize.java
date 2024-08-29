package pl.kerpson.motd.shared.feature.randomize;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import pl.kerpson.motd.shared.configuration.Configuration;
import pl.kerpson.motd.shared.configuration.section.MessageOfTheDayConfiguration;
import pl.kerpson.motd.shared.feature.MessageOfTheDay;

public class QueueMessageOfTheDayRandomize implements MessageOfTheDayRandomize {

  private final Configuration configuration;
  private Queue<MessageOfTheDay> messageOfTheDays;

  public QueueMessageOfTheDayRandomize(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void reload() {
    MessageOfTheDayConfiguration messageOfTheDayConfiguration = this.configuration.getMessageOfTheDayConfiguration();
    this.messageOfTheDays = new LinkedList<>(messageOfTheDayConfiguration.getMessageOfTheDay());
  }

  @Override
  public Optional<MessageOfTheDay> get() {
    MessageOfTheDayConfiguration messageOfTheDayConfiguration = this.configuration.getMessageOfTheDayConfiguration();
    if (messageOfTheDayConfiguration.getMessageOfTheDay().isEmpty()) {
      return Optional.empty();
    }

    if (this.messageOfTheDays.isEmpty()) {
      return Optional.empty();
    }

    if (this.messageOfTheDays.size() == 1) {
      return Optional.of(this.messageOfTheDays.element());
    }

    MessageOfTheDay messageOfTheDay = this.messageOfTheDays.poll();
    this.messageOfTheDays.offer(messageOfTheDay);

    return Optional.ofNullable(messageOfTheDay);
  }
}
