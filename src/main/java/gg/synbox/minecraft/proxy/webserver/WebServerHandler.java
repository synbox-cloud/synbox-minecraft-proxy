package gg.synbox.minecraft.proxy.webserver;

import gg.synbox.minecraft.proxy.SynboxProxy;
import gg.synbox.minecraft.proxy.velocity.WebhookEvent;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerKillEvent;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerStartEvent;
import gg.synbox.minecraft.proxy.velocity.events.SynboxServerStopEvent;
import io.javalin.Javalin;
import io.javalin.http.Context;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bson.Document;
import org.slf4j.Logger;

public class WebServerHandler {

    private final Logger logger;
    private final SynboxProxy server;

    public WebServerHandler(Logger logger, SynboxProxy server) {
        this.logger = logger;
        this.server = server;
    }

    public CompletableFuture<Object> handleWebhook(Context ctx) {
        return new CompletableFuture<>().completeAsync( () -> {
            String serverId = ctx.pathParam("serverId");
            try {
                // JSON parsen
                WebhookEvent webhookEvent = ctx.bodyAsClass(WebhookEvent.class);

                logger.info("Webhook empfangen für Server {}: Event={}, EventId={}",
                        serverId, webhookEvent.event(), webhookEvent.eventId());

                // Event direkt feuern basierend auf Event-Typ
                switch (webhookEvent.event()) {
                    case "SERVER_START" ->
                            server.getServer().getEventManager().fireAndForget(new SynboxServerStartEvent(
                                    serverId, webhookEvent.displayName(), webhookEvent.eventId()));
                    case "SERVER_STOP" ->
                            server.getServer().getEventManager().fireAndForget(new SynboxServerStopEvent(
                                    serverId, webhookEvent.displayName(), webhookEvent.eventId()));
                    case "SERVER_KILL" ->
                            server.getServer().getEventManager().fireAndForget(new SynboxServerKillEvent(
                                    serverId, webhookEvent.displayName(), webhookEvent.eventId()));
                    default ->
                            logger.warn("Unbekannter Event-Typ: {} für Server {}", webhookEvent.event(), serverId);
                }

                ctx.status(200).result("OK");
            } catch (Exception e) {
                logger.error("Fehler beim Verarbeiten des Webhooks für Server " + serverId, e);
                ctx.status(400).result("Bad Request: " + e.getMessage());
            }
            return null;
        });
    }

    public CompletableFuture<Object> handleMagicLink(Context ctx) {
        return new CompletableFuture<>().completeAsync(() -> {
            try {
                String uuid = ctx.pathParam("uuid");
                Document body = Document.parse(ctx.body());
                String link = body.getString("magicLink");
                server.getServer().getPlayer(UUID.fromString(uuid)).ifPresent(player -> {
                    player.sendMessage(Component.text("Klicke hier um dich einzuloggen!", NamedTextColor.AQUA)
                            .decorate(TextDecoration.BOLD).clickEvent(ClickEvent.openUrl(link)));
                });

                ctx.status(200).result("OK");
            } catch (Exception e) {
                ctx.status(400).result("Bad Request: " + e.getMessage());
            }
            return null;
        });
    }
    

}
