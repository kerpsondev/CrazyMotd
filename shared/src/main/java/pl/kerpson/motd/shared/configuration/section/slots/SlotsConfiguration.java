package pl.kerpson.motd.shared.configuration.section.slots;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SlotsConfiguration extends OkaeriConfig {

  @Comment({
      "PL: Typ slotów",
      "    REAL - Prawdziwa ilość slotów",
      "    DYNAMIC - Dynamiczna liczba slotów (aktualna liczba osób + x)",
      "    FAKE - Nieprawdziwa liczba slotów",
      "EN: Type of slots",
      "    REAL - Real number of slots",
      "    DYNAMIC - Dynamic number of slots (current number of people + x)",
      "    FAKE - Untrue number of slots"
  })
  @CustomKey("slots-type")
  private SlotType slotsType = SlotType.REAL;

  @Comment({"PL: Typ dynamicznych slotów",
      "    ADD_ONE - Dodaje o jeden slot więcej niż liczba graczy online",
      "    ADD_PERCENT - Dodaje procent do ilości osób online (np. jeżeli jest 100 osób i damy procent na 10 wyświetli się 110 slotów)",
      "    ROUNDING - Zaokrąglenie liczby slotów (np. jeżeli jest 11 osób to liczba slotów zostanie ustawiona na 20)",
      "EN: Dynamic Slots Type",
      "    ADD_ONE - Adds one slot more than the number of players online",
      "    ADD_PERCENT - adds a percentage to the number of people online (e.g. if there are 100 people and we give a percentage of 10 it will show 110 slots)",
      "    ROUNDING - Rounds up the number of slots (e.g. if there are 11 people the number of slots will be set to 20)"
  })
  @CustomKey("dynamic-rule")
  private DynamicRule dynamicRule = DynamicRule.ROUNDING;

  @Comment({
      "PL: Fałszywa liczba slotów",
      "EN: Fake slots amount"
  })
  @CustomKey("fake-slots")
  private int fakeSlots = 500;

  @Comment({
      "PL: Jaki procent slotów ma być dodawany do liczby slotów?",
      "EN: What percentage of slots should be added to the number of slots?"
  })
  @CustomKey("percent")
  private double percent = 10.0;

  public enum DynamicRule {

    ADD_ONE,
    ADD_PERCENT,
    ROUNDING
  }

  public enum SlotType {

    REAL,
    DYNAMIC,
    FAKE
  }
}