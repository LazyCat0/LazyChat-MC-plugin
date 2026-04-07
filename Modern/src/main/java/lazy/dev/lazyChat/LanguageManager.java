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
    private final FileConfiguration lang;
    private final FileConfiguration fallbackLang;
    public LanguageManager(JavaPlugin plugin) {
        String defaultLang = plugin.getConfig().getString("options.language", "English");
        loadLanguages(plugin);
        this.lang = languages.get(defaultLang);
        this.fallbackLang = languages.get("English");
    }
    public void loadLanguages(JavaPlugin plugin) {
        File dir = new File(plugin.getDataFolder(), "lang");
        plugin.saveResource("lang/Russian.yml", true);
        plugin.saveResource("lang/English.yml", true);
        plugin.saveResource("lang/Ukrainian.yml", true);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.getName().endsWith(".yml")) continue;
            String lang = file.getName().replace(".yml", "");
            languages.put(lang, YamlConfiguration.loadConfiguration(file));
        }
    }

    public String getRaw(@NotNull String path) {
        String raw = lang.getString(path);
        if (raw == null && fallbackLang != null) {
            return fallbackLang.getString(path);
        }
        return raw;
    }
    public Component getFormated(@NotNull String path) {
        String raw = getRaw(path);
        if (raw.isEmpty() && fallbackLang != null) {
            return mm.deserialize(fallbackLang.getString(path));
        }
        return mm.deserialize(raw);
    }
}
// by LazyCato0o