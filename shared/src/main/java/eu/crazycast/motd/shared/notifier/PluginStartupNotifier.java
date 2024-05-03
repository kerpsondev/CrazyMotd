package eu.crazycast.motd.shared.notifier;

import eu.crazycast.motd.shared.provider.ServerProvider;
import eu.crazycast.motd.shared.util.ChatUtil;
import java.time.Duration;
import java.util.stream.Stream;
import net.kyori.adventure.audience.Audience;

public class PluginStartupNotifier {

  private final Audience console;
  private final ServerProvider serverProvider;

  public PluginStartupNotifier(Audience console, ServerProvider serverProvider) {
    this.console = console;
    this.serverProvider = serverProvider;
  }

  public void notify(Duration enableTime) {
    Stream.of(
        "",
        "                   <gradient:#FBE200:#FDA700><bold>CrazyMotd",
        String.format(" &a✓  &7Plugin was successfully enabled in &a%sms", enableTime.toMillis()),
        "",
        " &#09DEFB» &7Author: &#FDA700kerpson",
        String.format(" &#09DEFB» &7Version: &#FDA700%s", this.serverProvider.version()),
        " &#09DEFB» &7Website: &#FDA700https://github.com/kerpsondev",
        ""
    ).map(ChatUtil::deserialize).forEach(this.console::sendMessage);
  }
}
