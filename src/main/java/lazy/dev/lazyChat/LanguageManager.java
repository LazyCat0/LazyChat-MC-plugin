package lazy.dev.lazyChat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LanguageManager {
    private final Map<String, FileConfiguration> languages = new HashMap<>();
    private final MiniMessage mm = MiniMessage.miniMessage();
    private FileConfiguration lang;
    public LanguageManager(JavaPlugin plugin) {
        String defaultLang = plugin.getConfig().getString("lang", "en_US");
        loadLanguages(plugin);
    }
    public void loadLanguages(JavaPlugin plugin) {
        File dir = new File(plugin.getDataFolder(), "lang");
        if (!dir.exists()) dir.mkdirs();
        plugin.saveResource("lang/ru_RU.yml", true);
        plugin.saveResource("lang/en_US.yml", true);
        plugin.saveResource("lang/uk_UA.yml", true);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.getName().endsWith(".yml")) continue;
            String lang = file.getName().replace(".yml", "");
            languages.put(lang, YamlConfiguration.loadConfiguration(file));
        }
    }
    public Component get(String path) {
        String raw = lang.getString(path);
        return mm.deserialize(raw);
    }
    public String getNonFormated(String path) {
        String raw = lang.getString(path);
        return raw;
    }
}
