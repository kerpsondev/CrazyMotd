package pl.kerpson.motd.bungee.provider;

import pl.kerpson.motd.shared.provider.LoggerProvider;
import java.util.logging.Logger;

public class BungeeLoggerProvider implements LoggerProvider {

  private final Logger logger;

  public BungeeLoggerProvider(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void info(String message) {
    this.logger.info(message);
  }

  @Override
  public void warn(String message) {
    this.logger.warning(message);
  }

  @Override
  public void error(String message) {
    this.logger.severe(message);
  }
}