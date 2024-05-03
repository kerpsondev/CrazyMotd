package eu.crazycast.motd.shared.factory;

import eu.crazycast.motd.shared.provider.ServerProvider;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.serdes.okaeri.SerdesOkaeri;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import java.io.File;


public class ConfigurationFactory {

  private final ServerProvider serverProvider;

  public ConfigurationFactory(ServerProvider serverProvider) {
    this.serverProvider = serverProvider;
  }

  public <T extends OkaeriConfig> T createConfiguration(
      Class<T> clazz,
      File file,
      OkaeriSerdesPack serdesPack
  ) {
    try {
      return ConfigManager.create(clazz, it -> {
        it.withConfigurer(new YamlSnakeYamlConfigurer(), new SerdesCommons(), new SerdesOkaeri());
        it.withBindFile(file);
        if (serdesPack != null) {
          it.withSerdesPack(serdesPack);
        }

        it.withRemoveOrphans(true);
        it.saveDefaults();
        it.load(true);
      });
    } catch (OkaeriException exception) {
      exception.printStackTrace();
      this.serverProvider.shutdown();
      return null;
    }
  }
}
