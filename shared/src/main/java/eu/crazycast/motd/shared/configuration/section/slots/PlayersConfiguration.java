package eu.crazycast.motd.shared.configuration.section.slots;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import java.util.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayersConfiguration extends OkaeriConfig {

  @Comment({
      "PL: Typ graczy online",
      "    REAL - Prawdziwa ilość osób online",
      "    FAKE - Nieprawdziwa liczba osób online",
      "EN: Type of online players",
      "    REAL - Real number of people online",
      "    FAKE - Untrue number of online players"
  })
  @CustomKey("players-type")
  private PlayersType playersType = PlayersType.REAL;

  @Comment({
      "PL: Typ fake graczy",
      "    STATIC - Niezmienna liczba graczy online",
      "    RANDOM - Losowa ilość graczy online",
      "EN: Type of fake players",
      "    STATIC - Unchanging amount of online players",
      "    RANDOM - Random count online players"
  })
  @CustomKey("fake-players-type")
  private FakePlayersType fakePlayersType = FakePlayersType.STATIC;

  @Comment({
      "PL: Fake liczba graczy online",
      "EN: Untrue online players count"
  })
  @CustomKey("static-players")
  private int staticPlayers = 100;

  @Comment({
      "PL: Zakres, w jakim ma być ustawiana fałszywa liczba graczy online",
      "    Działa tylko gdy fałszywe sloty są ustawione na RANDOM",
      "EN: The range in which the false number of online players is to be set",
      " Only works when false slots are set to RANDOM"
  })
  @CustomKey("fake-players-range")
  private Map<String, Integer> fakePlayersRange = new HashMap<String, Integer>() {{
    this.put("min", 50);
    this.put("max", 100);
  }};

  public enum FakePlayersType {

    STATIC,
    RANDOM
  }

  public enum PlayersType {

    REAL,
    FAKE
  }
}
