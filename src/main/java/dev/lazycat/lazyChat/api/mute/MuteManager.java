package dev.lazycat.lazyChat.api.mute;

import dev.lazycat.lazyChat.LazyChat;
import dev.lazycat.lazyChat.api.mute.storages.MuteStorage;
import dev.lazycat.lazyChat.api.mute.storages.SqliteMuteStorage;
import dev.lazycat.lazyChat.api.mute.storages.YamlMuteStorage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MuteManager {
    private final MuteStorage storage;
    private final Map<UUID, MuteInfo> mutes = new ConcurrentHashMap<>();

    public MuteManager(LazyChat plugin, boolean useDatabase) {
        if (useDatabase) {
            storage = new SqliteMuteStorage(plugin);
        } else {
            storage = new YamlMuteStorage(plugin);
        }
        reload();
    }

    public void reload() {
        mutes.clear();
        Map<UUID, MuteInfo> loaded = storage.loadMutes();
        loaded.entrySet().removeIf(entry -> System.currentTimeMillis() > entry.getValue().getExpiry());
        mutes.putAll(loaded);
    }

    public void mute(UUID uuid, long durationMillis, String reason) {
        long expiry = System.currentTimeMillis() + durationMillis;
        MuteInfo info = new MuteInfo(expiry, reason);
        mutes.put(uuid, info);
        storage.saveMute(uuid, info);
    }

    public void unmute(UUID uuid) {
        mutes.remove(uuid);
        storage.removeMute(uuid);
    }

    public boolean isMuted(UUID uuid) {
        MuteInfo info = mutes.get(uuid);
        if (info == null) return false;
        if (System.currentTimeMillis() > info.getExpiry()) {
            unmute(uuid); // автоматическое снятие
            return false;
        }
        return true;
    }

    public String getMuteReason(UUID uuid) {
        MuteInfo info = mutes.get(uuid);
        if (info == null) return null;
        if (System.currentTimeMillis() > info.getExpiry()) {
            unmute(uuid);
            return null;
        }
        return info.getReason();
    }

    public long getMuteExpiry(UUID uuid) {
        MuteInfo info = mutes.get(uuid);
        if (info == null) return -1;
        if (System.currentTimeMillis() > info.getExpiry()) {
            unmute(uuid);
            return -1;
        }
        return info.getExpiry();
    }

    public void close() {
        storage.close();
    }
}