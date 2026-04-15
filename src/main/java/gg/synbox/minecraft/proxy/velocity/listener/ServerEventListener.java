package gg.synbox.minecraft.proxy.velocity.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.server.ServerInfo;
import de.synbox.invoker.ApiException;
import gg.synbox.minecraft.proxy.SynboxProxy;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerKillEvent;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerStartEvent;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerStopEvent;
import gg.synbox.minecraft.proxy.velocity.util.SynUtils;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.Optional;

import static gg.synbox.minecraft.proxy.SynboxProxy.getInstance;
import static gg.synbox.minecraft.proxy.SynboxProxy.getSynboxAPI;

public class ServerEventListener {

    @Inject
    private Logger logger;

    @Subscribe
    public void onServerStart(SynboxServerStartEvent event) throws ApiException {
        Optional.of(getSynboxAPI().serverManagement().getContainer(event.getServerId())).ifPresent(SynUtils::registerServer);
    }

    @Subscribe
    public void onServerStop(SynboxServerStopEvent event) throws ApiException {
        Optional.of(getSynboxAPI().serverManagement().getContainer(event.getServerId())).ifPresent(SynUtils::unregisterServer);

    }

    @Subscribe
    public void onServerKill(SynboxServerKillEvent event) throws ApiException {
        Optional.of(getSynboxAPI().serverManagement().getContainer(event.getServerId())).ifPresent(SynUtils::unregisterServer);
    }

}
