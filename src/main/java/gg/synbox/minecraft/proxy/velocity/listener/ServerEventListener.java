package gg.synbox.minecraft.proxy.velocity.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import de.synbox.invoker.ApiException;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerKillEvent;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerStartEvent;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerStopEvent;
import gg.synbox.minecraft.proxy.velocity.util.SynUtils;
import org.slf4j.Logger;

import java.util.Optional;

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
