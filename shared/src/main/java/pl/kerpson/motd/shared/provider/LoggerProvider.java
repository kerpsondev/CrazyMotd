package pl.kerpson.motd.shared.provider;

public interface LoggerProvider {

  void info(String message);

  void warn(String message);

  void error(String message);
}
