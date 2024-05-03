package eu.crazycast.motd.shared.configuration.section;

import eu.crazycast.motd.shared.feature.MessageOfTheDay;
import eu.okaeri.commons.indexedset.IndexedSet;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import eu.okaeri.configs.serdes.okaeri.indexedset.IndexedSetSpec;
import java.util.Arrays;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MessageOfTheDayConfiguration extends OkaeriConfig {

  @Comment({
      "PL: Czy funkcja ma być włączona?",
      "EN: Shout the function be enabled?"
  })
  @CustomKey("enable")
  private boolean enable = true;

  @Comment({
      "PL: Jak motd ma się zmieniać?",
      "    RANDOM - Pokazuje losowe motd z listy",
      "    QUEUE - Motd zmienia się po kolei",
      "EN: How should motd change?",
      "    RANDOM - Shows a random motd from a list",
      "    QUEUE - Motd changes one at a time"
  })
  @CustomKey("update-type")
  private Type updateType = Type.RANDOM;

  @Comment({
      "PL: Lista motd",
      "EN: Motd list"
  })
  @IndexedSetSpec
  @CustomKey("message-of-the-day")
  private IndexedSet<MessageOfTheDay, String> messageOfTheDay = IndexedSet.of(MessageOfTheDay::getKey,
      new MessageOfTheDay("1", Arrays.asList("&6&lCrazyMotd &8>>> &fConfigure motd!", "&bIt's very simple!")),
      new MessageOfTheDay("2", Arrays.asList("&6&lCrazyMotd &8>>> &aSecond motd", ""))
  );

  public enum Type {

    RANDOM,
    QUEUE
  }
}