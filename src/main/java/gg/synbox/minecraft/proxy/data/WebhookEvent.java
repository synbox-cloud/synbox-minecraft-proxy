package gg.synbox.minecraft.proxy.data;

public record WebhookEvent(
        String eventId,
        String displayName,
        String event,
        String serverId,
        long timestamp
) {
}
