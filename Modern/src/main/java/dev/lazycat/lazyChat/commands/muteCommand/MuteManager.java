package dev.lazycat.lazyChat.commands.muteCommand;

import dev.lazycat.lazyChat.LazyChat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MuteManager {
    private final File file;
    private final FileConfiguration config;
    private final Map<UUID, Long> mutes = new HashMap<>();
    private final LazyChat lazyChat;
    public MuteManager(LazyChat plugin) {
        this.lazyChat = plugin;
        this.file = new File(plugin.getDataFolder(), "data/muted_players.yml");
        if (!file.exists()) {
            plugin.saveResource("data/muted_players.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        LoadMuteList();
    }
    public void LoadMuteList() {
        if (config.getConfigurationSection("mutes") == null) return;
        for (String key : Objects.requireNonNull(config.getConfigurationSection("mutes")).getKeys(false)) {
            mutes.put(UUID.fromString(key), config.getLong("mutes." + key));
        }
    }
    public void mute(UUID uuid, long durationMillis) {
        long expiry = System.currentTimeMillis() + durationMillis;
        mutes.put(uuid, expiry);
        config.set("mutes." + uuid.toString(), expiry);
        save();
    }
    public void unmute(UUID uuid) {
        mutes.remove(uuid);
        config.set("mutes." + uuid.toString(), null);
        save();
    }

    public boolean isMuted(UUID uuid) {
        if (!mutes.containsKey(uuid)) return false;
        if (System.currentTimeMillis() > mutes.get(uuid)) {
            unmute(uuid);
            return false;
        }
        return true;
    }

    private void save() {
        try { config.save(file); } catch (IOException e) { lazyChat.getLogger().severe(e.toString()); }
    }
}
// By LazyCato0o