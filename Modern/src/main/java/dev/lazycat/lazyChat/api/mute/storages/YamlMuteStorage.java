package dev.lazycat.lazyChat.api.mute.storages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class YamlMuteStorage implements MuteStorage {
    private final File file;
    private final FileConfiguration config;
    private final JavaPlugin plugin;

    public YamlMuteStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data/muted_players.yml");
        if (!file.exists()) {
            plugin.saveResource("data/muted_players.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public Map<UUID, Long> loadMutes() {
        Map<UUID, Long> mutes = new HashMap<>();
        if (config.getConfigurationSection("mutes") == null) return mutes;
        for (String key : Objects.requireNonNull(config.getConfigurationSection("mutes")).getKeys(false)) {
            mutes.put(UUID.fromString(key), config.getLong("mutes." + key));
        }
        return mutes;
    }

    @Override
    public void saveMute(UUID uuid, long expiry) {
        config.set("mutes." + uuid.toString(), expiry);
        save();
    }

    @Override
    public void removeMute(UUID uuid) {
        config.set("mutes." + uuid.toString(), null);
        save();
    }

    @Override
    public boolean isMuted(UUID uuid) {
        Long expiry = getExpiry(uuid);
        return expiry != null && System.currentTimeMillis() <= expiry;
    }

    @Override
    public Long getExpiry(UUID uuid) {
        if (!config.contains("mutes." + uuid.toString())) return null;
        return config.getLong("mutes." + uuid.toString());
    }

    private void save() {
        try { config.save(file); } catch (IOException e) { plugin.getLogger().severe("Failed to save mutes: " + e.getMessage()); }
    }

    @Override
    public void close() {}
}

