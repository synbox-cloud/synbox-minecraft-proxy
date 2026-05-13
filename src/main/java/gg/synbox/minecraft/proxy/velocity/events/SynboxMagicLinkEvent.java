package gg.synbox.minecraft.proxy.velocity.events;

import java.util.UUID;

public class SynboxMagicLinkEvent {


    private final String magicToken;
    private final String magicLink;
    private final UUID uuid;
    private boolean sendDefaultMessage = false;

    public SynboxMagicLinkEvent(boolean sendDefaultMessage, String magicToken, String magicLink, UUID uuid) {
        this.magicToken = magicToken;
        this.magicLink = magicLink;
        this.uuid = uuid;
        this.sendDefaultMessage = sendDefaultMessage;
    }

    public String getMagicLink() {
        return magicLink;
    }

    public String getMagicToken() {
        return magicToken;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isSendDefaultMessage() {
        return sendDefaultMessage;
    }

    public void setSendDefaultMessage(boolean sendDefaultMessage) {
        this.sendDefaultMessage = sendDefaultMessage;
    }
}
