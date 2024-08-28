package pl.kerpson.motd.shared.placeholer;

import pl.kerpson.motd.shared.configuration.Configuration;
import pl.kerpson.motd.shared.configuration.section.slots.PlayersConfiguration;
import pl.kerpson.motd.shared.configuration.section.slots.SlotsConfiguration;
import pl.kerpson.motd.shared.provider.ServerProvider;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PluginPlaceholders {

  private final static Random RANDOM = new Random();
  private final static Pattern SERVER_PLACEHOLDER_PATTERN = Pattern.compile("\\{server:([^}]+)}");

  private final Configuration configuration;
  private final ServerProvider serverProvider;

  public PluginPlaceholders(Configuration configuration, ServerProvider serverProvider) {
    this.configuration = configuration;
    this.serverProvider = serverProvider;
  }

  protected int players() {
    PlayersConfiguration playersConfiguration = this.configuration.getVersionConfiguration().getPlayersConfiguration();
    if (playersConfiguration.getPlayersType() != PlayersConfiguration.PlayersType.FAKE) {
      return serverProvider.getPlayers();
    }

    if (playersConfiguration.getFakePlayersType() == PlayersConfiguration.FakePlayersType.STATIC) {
      return playersConfiguration.getStaticPlayers();
    }

    Map<String, Integer> fakePlayersRange = playersConfiguration.getFakePlayersRange();
    int min = fakePlayersRange.getOrDefault("min", 0);
    int max = fakePlayersRange.getOrDefault("max", 1);
    return RANDOM.nextInt(max - min) + min;
  }

  protected int slots() {
    SlotsConfiguration slotsConfiguration = this.configuration.getVersionConfiguration().getSlotsConfiguration();
    switch (slotsConfiguration.getSlotsType()) {
      case FAKE:
        return slotsConfiguration.getFakeSlots();
      case DYNAMIC:
        switch (slotsConfiguration.getDynamicRule()) {
          case ADD_ONE:
            return this.players() + 1;
          case ROUNDING:
            return this.players() % 10 == 0 ?
                (this.players() + 10) :
                ((int) Math.ceil(this.players() / 10.0) * 10);
          case ADD_PERCENT:
            return this.players() + (int) (this.players() * slotsConfiguration.getPercent() / 100.0);
        }

      default:
        return serverProvider.getSlots();
    }
  }

  protected abstract int getOnlineForServer(String serverName);

  public String append(String text) {
    if (text.contains("{players}")) {
      text = text.replace("{players}", String.valueOf(this.players()));
    }

    if (text.contains("{slots}")) {
      text = text.replace("{slots}", String.valueOf(this.slots()));
    }

    Matcher matcher = SERVER_PLACEHOLDER_PATTERN.matcher(text);
    while (matcher.find()) {
      String serverName = matcher.group(1);
      text = text.replace("{server:" + serverName + "}", String.valueOf(this.getOnlineForServer(serverName)));
    }

    return text;
  }
}
