package eu.crazycast.motd.shared.provider;

public interface ServerProvider {

  void shutdown();

  String version();

  int getPlayers();

  int getSlots();
}
