package gg.synbox.minecraft.proxy.velocity.events;

public class SynboxServerStopEvent {
    
    private final String serverId;
    private final String displayName;
    private final String eventId;

    public SynboxServerStopEvent(String serverId, String displayName, String eventId) {
        this.serverId = serverId;
        this.displayName = displayName;
        this.eventId = eventId;
    }

    public String getServerId() {
        return serverId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEventId() {
        return eventId;
    }
}
