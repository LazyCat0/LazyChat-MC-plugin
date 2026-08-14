package dev.lazycat.lazyChat.api.mute.storages;

import java.util.Map;
import java.util.UUID;

public interface MuteStorage {
    Map<UUID, Long> loadMutes();
    void saveMute(UUID uuid, long expiry);
    void removeMute(UUID uuid);
    boolean isMuted(UUID uuid);
    Long getExpiry(UUID uuid);
    void close();
}