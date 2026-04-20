package gg.synbox.minecraft.proxy.velocity.util;

import com.velocitypowered.api.proxy.server.ServerInfo;
import de.synbox.invoker.ApiException;
import de.synbox.model.CloudServerDTO;
import de.synbox.model.ContainerFilter;
import gg.synbox.minecraft.proxy.SynboxProxy;
import org.bson.Document;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class SynUtils {

    private static List<String> serverIds = new ArrayList<>();

    public static void registerServer(CloudServerDTO serverDTO) {

        if(serverDTO.getEnvs() == null)return;
        if(serverDTO.getIp() == null)return;
        if(serverDTO.getPort() == null)return;

        Document document = Document.parse(serverDTO.getEnvs().toJson());
        String serverName = document.getOrDefault("SERVER_NAME", serverDTO.getServerId()).toString();
        serverIds.add(serverDTO.getServerId());
        InetSocketAddress address = new InetSocketAddress(serverDTO.getIp(), serverDTO.getPort());
        ServerInfo serverInfo = new ServerInfo(serverName, address);

        if(SynboxProxy.getInstance().getServer().getAllServers().stream()
                .anyMatch(t -> t.getServerInfo().getName().equalsIgnoreCase(serverName) && isSameAddress(t.getServerInfo().getAddress(),address)))return;

        unregisterAllServersWithEqualAddress(address);
        System.out.println(serverInfo.getName() + " registered!");
        SynboxProxy.getInstance().getServer().registerServer(serverInfo);
    }

    public static void unregisterServer(CloudServerDTO serverDTO) {
        if(serverDTO.getIp() == null)return;
        if(serverDTO.getPort() == null)return;
        unregisterAllServersWithEqualAddress(new InetSocketAddress(serverDTO.getIp(), serverDTO.getPort()));
    }

    private static void unregisterAllServersWithEqualAddress(InetSocketAddress address) {
        SynboxProxy.getInstance()
                .getServer()
                .getAllServers().stream().filter(t -> isSameAddress(t.getServerInfo().getAddress(), address))
                .filter(t -> !serverIds.contains(t.getServerInfo().getName()))
                .forEach(s -> {
                    System.out.println(s.getServerInfo().getName() + " unregistered!");
                    SynboxProxy.getInstance().getServer().unregisterServer(s.getServerInfo());
                    serverIds.remove(s.getServerInfo().getName());
                });
    }

    private static boolean isSameAddress(InetSocketAddress address1, InetSocketAddress address2) {
        return address1.getAddress().equals(address2.getAddress()) && address1.getPort() == address2.getPort();
    }

    public static List<CloudServerDTO> getServersFromOrganization(String accountId) throws ApiException {
        return SynboxProxy.getSynboxAPI().serverManagement()
                .getContainers(List.of(ContainerFilter.ORGANIZATION))
                .stream()
                .filter(t -> accountId.equals(t.getAccountId()))
                .filter(t -> SynboxProxy.getInstance().getConfig().getOrganization().equals(t.getOrganization()))
                .toList();
    }

    public static List<CloudServerDTO> getAllServersFromOrganization() throws ApiException {
        return SynboxProxy.getSynboxAPI().serverManagement()
                .getContainers(List.of(ContainerFilter.ORGANIZATION))
                .stream()
                .filter(t -> SynboxProxy.getInstance().getConfig().getOrganization().equals(t.getOrganization()))
                .toList();
    }

}
