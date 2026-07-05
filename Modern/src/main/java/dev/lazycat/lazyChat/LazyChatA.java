package dev.lazycat.lazyChat;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class LazyChatA {
    public static void migrator(JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
            return;
        }
        plugin.reloadConfig();
        int currentVersion = plugin.getConfig().getInt("config-version", 1);
        int latestVersion = 5;

        if (currentVersion >= latestVersion) return;

        plugin.getLogger().info("Updating config from v" + currentVersion + " to v" + latestVersion);

        YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(configFile);

        YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(Objects.requireNonNull(plugin.getResource("config.yml")))
        );

        if (currentVersion <= 2) {
            if (oldConfig.contains("local-chat-radius")) {
                newConfig.set("chat-radius", oldConfig.getInt("local-chat-radius"));
            }
            if (oldConfig.contains("global-chat-prefix")) {
                newConfig.set("prefix", oldConfig.getString("global-chat-prefix"));
            }
            if (oldConfig.contains("enable-console-logging")) {
                newConfig.set("enable-console-logging", oldConfig.getBoolean("enable-console-logging"));
            }
            if (oldConfig.contains("lang")) {
                newConfig.set("language", oldConfig.getString("lang"));
            }
            if (oldConfig.contains("global-chat-format")) {
                newConfig.set("global-chat-format", oldConfig.getString("global-chat-format"));
            }
            if (oldConfig.contains("local-chat-format")) {
                newConfig.set("local-chat-format", oldConfig.getString("local-chat-format"));
            }
        }

        if (currentVersion <= 4) {
            newConfig.set("options.language", newConfig.getString("language"));
            newConfig.set("options.chat-radius", newConfig.getInt("chat-radius"));
            newConfig.set("options.enable-logging", newConfig.getBoolean("enable-console-logging"));
            newConfig.set("options.whisper-radius", 10);
            newConfig.set("templates.global", newConfig.getString("global-chat-format"));
            newConfig.set("templates.local", newConfig.getString("local-chat-format"));
            newConfig.set("chats.global.template", "templates.global");
            newConfig.set("chats.global.prefix", newConfig.getString("prefix"));
            newConfig.set("chats.local.template", "templates.local");
            newConfig.set("chats.local.prefix", "");
            newConfig.set("blacklist.colors", false);
            newConfig.set("blacklist.gradients", false);
            newConfig.set("blacklist.signs", false);
            newConfig.set("blacklist.dm", false);
            newConfig.set("blacklist.whisper", false);
            newConfig.set("blacklist.tags", java.util.Arrays.asList("newline", "obf"));

            newConfig.set("language", null);
            newConfig.set("chat-radius", null);
            newConfig.set("enable-console-logging", null);
            newConfig.set("global-chat-format", null);
            newConfig.set("local-chat-format", null);
            newConfig.set("prefix", null);
        }

        newConfig.set("config-version", latestVersion);

        try {
            newConfig.save(configFile);
            plugin.reloadConfig();
            plugin.getLogger().info("Config successfully updated to version " + latestVersion);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save updated config: " + e.getMessage());
        }
    }
}
