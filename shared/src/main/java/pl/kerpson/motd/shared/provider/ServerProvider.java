package pl.kerpson.motd.shared.provider;

public interface ServerProvider {

  void shutdown();

  String version();

  int getPlayers();

  int getSlots();
}
