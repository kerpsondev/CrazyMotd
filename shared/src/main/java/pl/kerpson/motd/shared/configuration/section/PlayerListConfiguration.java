package pl.kerpson.motd.shared.configuration.section;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerListConfiguration extends OkaeriConfig {

  @Comment({
      "PL: Czy funkcja ma być włączona?",
      "EN: Shout the function be enabled?"
  })
  @CustomKey("enable")
  private boolean enable = true;

  @Comment({
      "PL: Tekst wyświetlany po najechaniu na listę graczy",
      "EN: Text displayed when hovering over player list"
  })
  @CustomKey("player-list")
  private List<String> playerList = Arrays.asList("", " &6&lCrazyMotd &8>> &cOnline survival: &e{server:survival}", "");
}