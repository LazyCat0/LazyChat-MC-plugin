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
    public LanguageManager(JavaPlugin plugin) {
        String defaultLang = plugin.getConfig().getString("lang", "en_US");
        loadLanguages(plugin);
        this.lang = languages.get(defaultLang);
    }
    // [EN] Here we makes plugin save default and load exists language files
    // [RU] Тут мы заставляем плагин сохранить изначальные и загрузить существующие файлы языков
    public void loadLanguages(JavaPlugin plugin) {
        File dir = new File(plugin.getDataFolder(), "lang");
        plugin.saveResource("lang/ru_RU.yml", false);
        plugin.saveResource("lang/en_US.yml", false);
        plugin.saveResource("lang/uk_UA.yml", false);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.getName().endsWith(".yml")) continue;
            String lang = file.getName().replace(".yml", "");
            languages.put(lang, YamlConfiguration.loadConfiguration(file));
        }
    }

    // [EN] And here, we read strings from /lang/en_US.yml (en_US.yml for example). Also, we can choose: read raw or formatted.
    // [RU] И здесь мы читаем строки из /lang/en_US.yml (en_US.yml для примера). Также, мы можем выбрать: прочитать сырое (без форматирования MM) или с форматированием.
    public String getRaw(String path) {
        @NotNull String raw = Objects.requireNonNull(lang.getString(path));
        return raw;
    }
    public Component getFormated(@NotNull String path) {
        @NotNull String raw = Objects.requireNonNull(lang.getString(path));
        return mm.deserialize(raw);
    }
}
// by LazyCato0o