package gg.synbox.minecraft.proxy.velocity;

public record WebhookEvent(
        String eventId,
        String displayName,
        String event,
        String serverId,
        long timestamp
) {
}
