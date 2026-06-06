package gg.synbox.minecraft.proxy.velocity.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import de.synbox.invoker.ApiException;
import de.synbox.model.CloudServerDTO;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerKillEvent;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerStartEvent;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerStopEvent;
import gg.synbox.minecraft.proxy.velocity.util.SynUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Optional;

import static gg.synbox.minecraft.proxy.SynboxProxy.getSynboxAPI;

public class ServerEventListener {

    @Inject
    private Logger logger;

    public static HashMap<String, CloudServerDTO> serverMap = new HashMap<>();

    @Subscribe
    public void onServerStart(SynboxServerStartEvent event) throws ApiException {
        var server = getSynboxAPI().serverManagement().getContainer(event.getServerId());
        serverMap.put(event.getServerId(), server);
        SynUtils.registerServer(server);
    }

    @Subscribe
    public void onServerStop(SynboxServerStopEvent event) throws ApiException {
        if(serverMap.containsKey(event.getServerId())) {
            SynUtils.unregisterServer(serverMap.get(event.getServerId()));
            serverMap.remove(event.getServerId());
        }
    }

    @Subscribe
    public void onServerKill(SynboxServerKillEvent event) throws ApiException {
        if(serverMap.containsKey(event.getServerId())) {
            SynUtils.unregisterServer(serverMap.get(event.getServerId()));
            serverMap.remove(event.getServerId());
        }
    }

}
