package pl.kerpson.motd.shared.util;

import net.kyori.adventure.text.serializer.legacy.*;
import net.kyori.adventure.text.minimessage.*;
import java.util.function.*;
import java.util.regex.*;
import net.kyori.adventure.text.*;

public final class ChatUtil {

  private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.legacyAmpersand()
      .toBuilder()
      .hexCharacter('#')
      .useUnusualXRepeatedCharacterHexFormat()
      .build();

  private static final LegacyComponentSerializer SECTION_SERIALIZER = LegacyComponentSerializer.legacySection()
      .toBuilder()
      .hexCharacter('#')
      .useUnusualXRepeatedCharacterHexFormat()
      .build();

  private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
      .preProcessor(new PreProcessor())
      .postProcessor(new PostProcessor())
      .build();

  private ChatUtil() {}

  public static Component deserialize(String message) {
    return ChatUtil.MINI_MESSAGE.deserialize(message);
  }

  public static String serialize(Component message) {
    return ChatUtil.MINI_MESSAGE.serialize(message);
  }

  public static Component deserializeAmpersand(String message) {
    return ChatUtil.AMPERSAND_SERIALIZER.deserialize(message);
  }

  public static String serializeAmpersand(Component message) {
    return ChatUtil.AMPERSAND_SERIALIZER.serialize(message);
  }

  public static Component deserializeSection(String message) {
    return ChatUtil.SECTION_SERIALIZER.deserialize(message);
  }

  public static String serializeSection(Component message) {
    return ChatUtil.SECTION_SERIALIZER.serialize(message);
  }

  private static class PostProcessor implements UnaryOperator<Component> {

    @Override
    public Component apply(Component component) {
      return component.replaceText(builder -> builder.match(Pattern.compile(".*"))
          .replacement((matchResult, builder1) -> ChatUtil.deserializeAmpersand(matchResult.group()))
      );
    }
  }

  private static class PreProcessor implements UnaryOperator<String> {

    @Override
    public String apply(String s) {
      return s.replace('§', '&');
    }
  }
}