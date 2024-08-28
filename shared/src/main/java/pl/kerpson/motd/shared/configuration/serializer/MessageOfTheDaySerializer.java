package pl.kerpson.motd.shared.configuration.serializer;

import pl.kerpson.motd.shared.feature.MessageOfTheDay;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class MessageOfTheDaySerializer implements ObjectSerializer<MessageOfTheDay> {

  @Override
  public boolean supports(@NonNull Class<? super MessageOfTheDay> type) {
    return MessageOfTheDay.class.isAssignableFrom(type);
  }

  @Override
  public void serialize(
      @NonNull MessageOfTheDay object,
      @NonNull SerializationData data,
      @NonNull GenericsDeclaration generics
  ) {
    data.add("key", object.getKey());
    data.addCollection("lines", object.getLines(), String.class);
  }

  @Override
  public MessageOfTheDay deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
    return new MessageOfTheDay(
        data.get("key", String.class),
        data.getAsList("lines", String.class)
    );
  }
}