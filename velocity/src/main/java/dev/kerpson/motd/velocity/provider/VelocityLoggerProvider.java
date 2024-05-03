package dev.kerpson.motd.velocity.provider;

import eu.crazycast.motd.shared.provider.LoggerProvider;
import org.slf4j.Logger;

public class VelocityLoggerProvider implements LoggerProvider {

  private final Logger logger;

  public VelocityLoggerProvider(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void info(String message) {
    this.logger.info(message);
  }

  @Override
  public void warn(String message) {
    this.logger.warn(message);
  }

  @Override
  public void error(String message) {
    this.logger.error(message);
  }
}