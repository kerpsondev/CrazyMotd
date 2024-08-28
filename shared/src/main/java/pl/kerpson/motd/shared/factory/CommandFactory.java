package pl.kerpson.motd.shared.factory;

import pl.kerpson.motd.shared.provider.LoggerProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class CommandFactory<T> {

  private final LoggerProvider loggerProvider;
  protected Set<T> commands;

  public CommandFactory(LoggerProvider loggerProvider) {
    this.loggerProvider = loggerProvider;
    this.commands = new HashSet<>();
  }

  @SafeVarargs
  public final void addCommands(T... t) {
    this.commands.addAll(Arrays.asList(t));
  }

  protected abstract void register();

  protected abstract void unregister();

  public void registerCommands() {
    this.register();
    this.loggerProvider.info("Successfully register commands");
  }

  public void unregisterCommands() {
    this.unregister();
    this.commands = null;
    this.loggerProvider.info("Unregister commands");
  }
}
