package dev.lazycat.lazyChat.api.mute;

import dev.lazycat.lazyChat.api.mute.storages.MuteStorage;
import dev.lazycat.lazyChat.api.mute.storages.SqliteMuteStorage;
import dev.lazycat.lazyChat.api.mute.storages.YamlMuteStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MuteManager {
    private final MuteStorage storage;
    private final Map<UUID, Long> mutes = new ConcurrentHashMap<>();

    public MuteManager(JavaPlugin plugin, boolean useDatabase) {
        if (useDatabase) {
            storage = new SqliteMuteStorage(plugin);
        } else {
            storage = new YamlMuteStorage(plugin);
        }
        reload();
    }

    public void reload() {
        mutes.clear();
        Map<UUID, Long> loaded = storage.loadMutes();
        loaded.entrySet().removeIf(entry -> System.currentTimeMillis() > entry.getValue());
        mutes.putAll(loaded);
    }

    public void mute(UUID uuid, long durationMillis) {
        long expiry = System.currentTimeMillis() + durationMillis;
        mutes.put(uuid, expiry);
        storage.saveMute(uuid, expiry);
    }

    public void unmute(UUID uuid) {
        mutes.remove(uuid);
        storage.removeMute(uuid);
    }

    public boolean isMuted(UUID uuid) {
        if (!mutes.containsKey(uuid)) return false;
        long expiry = mutes.get(uuid);
        if (System.currentTimeMillis() > expiry) {
            unmute(uuid);
            return false;
        }
        return true;
    }

    public void close() {
        storage.close();
    }
}
