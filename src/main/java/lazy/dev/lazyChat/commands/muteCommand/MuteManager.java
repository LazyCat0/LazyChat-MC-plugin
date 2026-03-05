package lazy.dev.lazyChat.commands.muteCommand;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager {
    private final File file;
    private final FileConfiguration config;
    private final Map<UUID, Long> mutes = new HashMap<>();
    public MuteManager(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "muted_players.yml");
        if (!file.exists()) {
            plugin.saveResource("muted_players.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        LoadMuteList();
    }
    public void LoadMuteList() {
        if (config.getConfigurationSection("mutes") == null) return;
        for (String key : config.getConfigurationSection("mutes").getKeys(false)) {
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
            unmute(uuid); // Срок истек
            return false;
        }
        return true;
    }

    private void save() {
        try { config.save(file); } catch (IOException e) { e.printStackTrace(); }
    }
}
// By LazyCato0o