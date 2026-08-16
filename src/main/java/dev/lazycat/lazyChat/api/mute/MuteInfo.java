package dev.lazycat.lazyChat.api.mute;

import net.kyori.adventure.text.Component;

public class MuteInfo {
    private final long expiry;
    private final String reason;

    public MuteInfo(long expiry, String reason) {
        this.expiry = expiry;
        this.reason = reason;
    }

    public long getExpiry() {
        return expiry;
    }

    public String getReason() {
        return reason;
    }
}