package dev.kerpson.motd.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import eu.crazycast.motd.shared.configuration.Configuration;
import eu.crazycast.motd.shared.configuration.serializer.MessageOfTheDaySerializer;
import eu.crazycast.motd.shared.factory.CommandFactory;
import eu.crazycast.motd.shared.factory.ConfigurationFactory;
import eu.crazycast.motd.shared.factory.ListenerFactory;
import eu.crazycast.motd.shared.notifier.PluginStartupNotifier;
import eu.crazycast.motd.shared.notifier.PluginUpdater;
import eu.crazycast.motd.shared.provider.LoggerProvider;
import eu.crazycast.motd.shared.provider.ServerProvider;
import eu.crazycast.motd.shared.util.Optionals;
import dev.kerpson.motd.velocity.command.CrazyMotdCommand;
import dev.kerpson.motd.velocity.factory.VelocityCommandFactory;
import dev.kerpson.motd.velocity.factory.VelocityListenerFactory;
import dev.kerpson.motd.velocity.feature.VelocityMessageOfTheDayService;
import dev.kerpson.motd.velocity.listener.ProxyPingListener;
import dev.kerpson.motd.velocity.listener.ServerConnectListener;
import dev.kerpson.motd.velocity.placeholder.VelocityPluginPlaceholders;
import dev.kerpson.motd.velocity.provider.VelocityLoggerProvider;
import dev.kerpson.motd.velocity.provider.VelocityServerProvider;
import eu.okaeri.configs.exception.OkaeriException;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.bstats.velocity.Metrics.Factory;
import org.slf4j.Logger;

@Plugin(
    id = "crazymotd",
    name = "crazymotd",
    version = "1.0.1",
    url = "github.com/kerpsondev/CrazyMotd",
    description = "Simple motd plugin",
    authors = {"kerpson"}
)
public class VelocityMotd {

  private final File configFile = new File("plugins/crazymotd", "configuration.yml");

  private final Logger logger;
  private final Factory factory;
  private final ProxyServer proxyServer;

  private Configuration configuration;
  private PluginUpdater pluginUpdater;
  private LoggerProvider loggerProvider;
  private ServerProvider serverProvider;
  private VelocityCommandFactory commandFactory;
  private VelocityListenerFactory listenerFactory;
  private VelocityMessageOfTheDayService messageOfTheDayService;

  private Optional<ScheduledTask> messageOfTheDayTask = Optional.empty();

  @Inject
  public VelocityMotd(Logger logger, Factory factory, ProxyServer proxyServer) {
    this.logger = logger;
    this.factory = factory;
    this.proxyServer = proxyServer;
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    this.factory.make(this, 21090);

    Instant startEnableTime = Instant.now();
    this.loggerProvider = new VelocityLoggerProvider(this.logger);
    this.serverProvider = new VelocityServerProvider(this.proxyServer);
    this.createConfiguration();

    VelocityPluginPlaceholders pluginPlaceholders = new VelocityPluginPlaceholders(
        this.configuration,
        this.serverProvider,
        this.proxyServer
    );

    this.messageOfTheDayService = new VelocityMessageOfTheDayService(
        this.configuration,
        pluginPlaceholders
    );

    this.pluginUpdater = new PluginUpdater(this.proxyServer, this.serverProvider);

    this.registerCommands();
    this.registerListeners();
    this.setupMessageOfTheDayTask();

    PluginStartupNotifier pluginStartupNotifier = new PluginStartupNotifier(this.proxyServer, this.serverProvider);
    pluginStartupNotifier.notify(Duration.between(startEnableTime, Instant.now()));

    this.pluginUpdater.check();
  }

  @Subscribe
  public void onProxyShutdown(ProxyShutdownEvent event) {
    Optional.ofNullable(this.commandFactory).ifPresent(CommandFactory::unregisterCommands);
    Optional.ofNullable(this.listenerFactory).ifPresent(ListenerFactory::unregisterListeners);
    this.proxyServer.getScheduler().tasksByPlugin(this).forEach(ScheduledTask::cancel);
  }

  private void registerCommands() {
    this.commandFactory = new VelocityCommandFactory(this.proxyServer, this.loggerProvider);
    this.commandFactory.addCommands(new CrazyMotdCommand(this, this.configuration));
    this.commandFactory.registerCommands();
  }

  private void registerListeners() {
    this.listenerFactory = new VelocityListenerFactory(this.loggerProvider, this, this.proxyServer);
    this.listenerFactory.addListeners(
        new ProxyPingListener(this.messageOfTheDayService),
        new ServerConnectListener(this.pluginUpdater)
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

    this.logger.info("Successfully created configuration");
  }

  private void setupMessageOfTheDayTask() {
    this.messageOfTheDayTask.ifPresent(ScheduledTask::cancel);
    this.messageOfTheDayTask = Optionals.when(
        !this.configuration.getUpdateTime().isNegative() && !this.configuration.getUpdateTime().isZero(),
        this.proxyServer.getScheduler()
            .buildTask(this, () -> this.messageOfTheDayService.update())
            .repeat(this.configuration.getUpdateTime())
            .schedule()
    );
  }

  public void reloadConfiguration() throws OkaeriException {
    this.configuration.load(true);
    this.setupMessageOfTheDayTask();
  }
}