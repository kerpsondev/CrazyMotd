package eu.crazycast.motd.shared.feature;

import java.util.List;

public class MessageOfTheDay {

  private final String key;
  private final List<String> lines;

  public MessageOfTheDay(String key, List<String> lines) {
    this.key = key;
    this.lines = lines;
    this.sublistList();
  }

  public String getKey() {
    return this.key;
  }

  public List<String> getLines() {
    return this.lines;
  }

  private void sublistList() {
    if (this.lines.size() > 2) {
      this.lines.subList(2, this.lines.size()).clear();
    }
  }
}
