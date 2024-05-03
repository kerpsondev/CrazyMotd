package dev.kerpson.motd.bungee;

import dev.kerpson.motd.bungee.command.CrazyMotdCommand;
import dev.kerpson.motd.bungee.factory.BungeeCommandFactory;
import dev.kerpson.motd.bungee.factory.BungeeListenerFactory;
import dev.kerpson.motd.bungee.feature.BungeeMessageOfTheDayService;
import dev.kerpson.motd.bungee.listener.ProxyPingListener;
import dev.kerpson.motd.bungee.listener.ServerConnectListener;
import dev.kerpson.motd.bungee.placeholder.BungeePluginPlaceholders;
import dev.kerpson.motd.bungee.provider.BungeeLoggerProvider;
import dev.kerpson.motd.bungee.provider.BungeeServerProvider;
import eu.crazycast.motd.shared.configuration.Configuration;
import eu.crazycast.motd.shared.configuration.serializer.MessageOfTheDaySerializer;
import eu.crazycast.motd.shared.factory.CommandFactory;
import eu.crazycast.motd.shared.factory.ConfigurationFactory;
import eu.crazycast.motd.shared.factory.ListenerFactory;
import eu.crazycast.motd.shared.notifier.PluginStartupNotifier;
import eu.crazycast.motd.shared.notifier.PluginUpdater;
import eu.crazycast.motd.shared.placeholer.PluginPlaceholders;
import eu.crazycast.motd.shared.provider.LoggerProvider;
import eu.crazycast.motd.shared.provider.ServerProvider;
import eu.crazycast.motd.shared.util.Optionals;
import eu.okaeri.configs.exception.OkaeriException;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.bstats.bungeecord.Metrics;

public class BungeeMotd extends Plugin {

  private final File configFile = new File("plugins/crazymotd", "configuration.yml");

  private ProxyServer proxyServer;

  private BungeeAudiences adventure;
  private PluginUpdater pluginUpdater;
  private Configuration configuration;
  private LoggerProvider loggerProvider;
  private ServerProvider serverProvider;
  private BungeeCommandFactory commandFactory;
  private BungeeListenerFactory listenerFactory;
  private BungeeMessageOfTheDayService messageOfTheDayService;

  private Optional<ScheduledTask> messageOfTheDayTask = Optional.empty();

  @Override
  public void onEnable() {
    new Metrics(this, 21091);

    Instant startEnableTime = Instant.now();
    this.adventure = BungeeAudiences.create(this);
    this.proxyServer = this.getProxy();
    this.loggerProvider = new BungeeLoggerProvider(this.getLogger());
    this.serverProvider = new BungeeServerProvider(this.proxyServer);
    this.createConfiguration();

    PluginPlaceholders pluginPlaceholders = new BungeePluginPlaceholders(
        this.configuration,
        this.serverProvider,
        this.proxyServer
    );

    this.messageOfTheDayService = new BungeeMessageOfTheDayService(
        this.configuration,
        pluginPlaceholders
    );

    this.pluginUpdater = new PluginUpdater(this.adventure.console(), this.serverProvider);

    this.registerCommands();
    this.registerListeners();
    this.setupMessageOfTheDayTask();

    PluginStartupNotifier pluginStartupNotifier = new PluginStartupNotifier(this.adventure.console(), this.serverProvider);
    pluginStartupNotifier.notify(Duration.between(startEnableTime, Instant.now()));

    this.pluginUpdater.check();
  }

  @Override
  public void onDisable() {
    Optional.ofNullable(this.adventure).ifPresent(BungeeAudiences::close);
    Optional.ofNullable(this.commandFactory).ifPresent(CommandFactory::unregisterCommands);
    Optional.ofNullable(this.listenerFactory).ifPresent(ListenerFactory::unregisterListeners);
    this.proxyServer.getScheduler().cancel(this);
  }

  private void registerCommands() {
    this.commandFactory = new BungeeCommandFactory(this, this.loggerProvider);
    this.commandFactory.addCommands(new CrazyMotdCommand(
        this,
        this.adventure,
        this.configuration
    ));

    this.commandFactory.registerCommands();
  }

  private void registerListeners() {
    this.listenerFactory = new BungeeListenerFactory(this.loggerProvider, this, this.proxyServer);
    this.listenerFactory.addListeners(
        new ProxyPingListener(this.messageOfTheDayService),
        new ServerConnectListener(this.adventure, this.pluginUpdater)
    );

    this.listenerFactory.registerListeners();
  }

  private void createConfiguration() {
    ConfigurationFactory configurationFactory = new ConfigurationFactory(this.serverProvider);
    this.configuration = configurationFactory.createConfiguration(
        Configuration.class,
        this.configFile,
        registry -> registry.register(new MessageOfTheDaySerializer())
    );

    this.getLogger().info("Successfully created configuration");
  }

  private void setupMessageOfTheDayTask() {
    this.messageOfTheDayTask.ifPresent(ScheduledTask::cancel);
    this.messageOfTheDayTask = Optionals.when(
        !this.configuration.getUpdateTime().isNegative() && !this.configuration.getUpdateTime().isZero(),
        this.proxyServer.getScheduler().schedule(
            this,
            () -> this.messageOfTheDayService.update(),
            0L,
            this.configuration.getUpdateTime().getSeconds(),
            TimeUnit.SECONDS
        )
    );
  }

  public void reloadConfiguration() throws OkaeriException {
    this.configuration.load(true);
    this.setupMessageOfTheDayTask();
  }
}
