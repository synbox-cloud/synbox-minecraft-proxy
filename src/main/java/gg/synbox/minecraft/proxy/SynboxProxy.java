package gg.synbox.minecraft.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.synbox.api.ApiFacade;
import de.synbox.model.UserDTO;
import gg.synbox.minecraft.proxy.config.Config;
import gg.synbox.minecraft.proxy.velocity.listener.ServerEventListener;
import gg.synbox.minecraft.proxy.velocity.util.SynUtils;
import gg.synbox.minecraft.proxy.webserver.WebServerHandler;
import io.javalin.Javalin;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.time.Duration;

@Plugin(id = "synboxproxy", name = "SynboxProxy", version = "1.0")
public class SynboxProxy {

    private final Logger logger;

    private final Config config;
    private final ProxyServer server;
    private static SynboxProxy instance;
    private static ApiFacade synboxAPI;

    @Inject
    public SynboxProxy(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        instance = this;
        this.server = server;
        this.logger = logger;
        this.config = new Config(dataDirectory, logger);
        config.load();

        WebServerHandler webServerHandler = new WebServerHandler(logger, this);

        Javalin.create(config -> {
            config.routes.post("/{serverId}", ctx -> {
                ctx.future(() -> webServerHandler.handleWebhook(ctx));
            });
            config.routes.post("/magicLink/{uuid}", ctx -> {
                ctx.future(() -> webServerHandler.handleMagicLink(ctx));
            });
        }).start("0.0.0.0", config.getWebserverPort());

        if(config.getApiKey() == null) {
            logger.error("API Key ist nicht in der config.yml gesetzt! Bitte setze den API Key und starte den Proxy neu.");
            return;
        }

        synboxAPI = new ApiFacade("https://synbox.cloud:8080", config.getApiKey());;

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        getServer().getEventManager().register(this, new ServerEventListener());

        try {
            UserDTO userDTO = synboxAPI.userManagement().getUserInformation();
            logger.info("Erfolgreich mit der Synbox API verbunden! Angemeldeter Benutzer: {}", userDTO.getUserid());
        } catch (Exception e) {
            logger.error("Fehler beim Verbinden mit der Synbox API: {}", e.getMessage());
        }
        getServer().getScheduler().buildTask(this, () -> {
            try {
                synboxAPI.serverManagement().getContainers().stream().filter(t -> t.getOrganization() != null && t.getOrganization().equalsIgnoreCase(config.getOrganization())).forEach(SynUtils::registerServer);
                logger.info("Erfolgreich mit der Synbox API verbunden!");
            } catch (Exception e) {
                logger.error("Fehler beim Verbinden mit der Synbox API: {}", e.getMessage());
            }
        }).repeat(Duration.ofSeconds(config.getRefreshInterval())).schedule();
    }

    public static ApiFacade getSynboxAPI() {
        return synboxAPI;
    }

    public ProxyServer getServer() {
        return server;
    }

    public static SynboxProxy getInstance() {
        return instance;
    }
}
