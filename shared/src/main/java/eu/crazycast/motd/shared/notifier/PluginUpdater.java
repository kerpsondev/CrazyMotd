package eu.crazycast.motd.shared.notifier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.crazycast.motd.shared.provider.ServerProvider;
import eu.crazycast.motd.shared.util.ChatUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Stream;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class PluginUpdater {

  private final Audience console;
  private final ServerProvider serverProvider;

  private boolean needUpdate;
  private String latestVersion;

  public PluginUpdater(Audience console, ServerProvider serverProvider) {
    this.console = console;
    this.serverProvider = serverProvider;
  }

  public void notify(Audience audience) {
    if (!this.needUpdate) {
      return;
    }

    Stream.of(
        "",
        "            <gradient:#FBE200:#FDA700><bold>CrazyMotd",
        String.format("&8[&e#&8] &cA newer version of the plugin is available! (%s)", latestVersion),
        "&8[&e#&8] &cDownload it from: &bhttps://github.com/kerpsondev/CrazyMotd/releases/latest"
    ).map(ChatUtil::deserialize).forEach(audience::sendMessage);
  }

  public void check() {
    this.console.sendMessage(ChatUtil.deserialize(" &#00E1F7ˇˇˇ Update checker... ˇˇˇ"));

    try {
      String currentVersion = serverProvider.version();
      this.latestVersion = this.getLatestVersion();

      if (latestVersion.equals(currentVersion)) {
        this.needUpdate = false;
        this.console.sendMessage(ChatUtil.deserialize(" &aYou are using the latest version of the plugin!"));
      } else {
        this.needUpdate = true;
        this.console.sendMessage(ChatUtil.deserialize(String.format(" &cA newer version of the plugin is available! (%s)", this.latestVersion)));
        this.console.sendMessage(ChatUtil.deserialize(" &cDownload it from: &bhttps://github.com/kerpsondev/CrazyMotd/releases/latest"));
      }
    } catch (IOException ignored) {
      this.needUpdate = false;
      this.console.sendMessage(ChatUtil.deserialize(" &cWarning: There was an error while checking the version! Check your internet connection."));
    }

    this.console.sendMessage(Component.empty());
  }

  private String getLatestVersion() throws IOException {
    URL url = new URL("https://api.github.com/repos/kerpsondev/CrazyMotd/releases/latest");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      JsonObject jsonResponse = JsonParser.parseReader(in).getAsJsonObject();
      return jsonResponse.get("tag_name").getAsString();
    }
  }
}
