package eu.crazycast.motd.shared.factory;

import eu.crazycast.motd.shared.provider.LoggerProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class ListenerFactory<T> {

  private final LoggerProvider loggerProvider;
  protected Set<T> listeners;

  public ListenerFactory(LoggerProvider loggerProvider) {
    this.loggerProvider = loggerProvider;
    this.listeners = new HashSet<>();
  }

  @SafeVarargs
  public final void addListeners(T... t) {
    this.listeners.addAll(Arrays.asList(t));
  }

  protected abstract void register();

  protected abstract void unregister();

  public void registerListeners() {
    this.register();
    this.loggerProvider.info("Successfully register listeners");
  }

  public void unregisterListeners() {
    this.unregister();
    this.listeners = null;
    this.loggerProvider.info("Unregister listeners");
  }
}
