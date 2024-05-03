package eu.crazycast.motd.shared.util;

import java.util.Optional;

public final class Optionals {

  private Optionals() {}

  public static <T> Optional<T> when(boolean b, T t) {
    if (!b) {
      return Optional.empty();
    }

    return Optional.of(t);
  }
}
