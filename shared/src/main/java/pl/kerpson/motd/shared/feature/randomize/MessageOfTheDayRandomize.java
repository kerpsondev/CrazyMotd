package pl.kerpson.motd.shared.feature.randomize;

import java.util.Optional;
import pl.kerpson.motd.shared.feature.MessageOfTheDay;

public interface MessageOfTheDayRandomize {

  void reload();

  Optional<MessageOfTheDay> get();
}
