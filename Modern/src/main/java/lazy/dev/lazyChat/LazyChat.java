package lazy.dev.lazyChat;

import lazy.dev.lazyChat.chatSystem.ChatUtility;
import lazy.dev.lazyChat.chatSystem.lcManager;
import lazy.dev.lazyChat.commands.BCCommand;
import lazy.dev.lazyChat.commands.LCCommand;
import lazy.dev.lazyChat.commands.LCCommandCompleter;
import lazy.dev.lazyChat.commands.muteCommand.MuteCommand;
import lazy.dev.lazyChat.commands.muteCommand.MuteCommandCompleter;
import lazy.dev.lazyChat.commands.muteCommand.MuteManager;
import lazy.dev.lazyChat.sign.SignListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public final class LazyChat extends JavaPlugin {
    public String currentVersion = this.getPluginMeta().getVersion();
    public static LazyChat instance;
    public LanguageManager languageManager;
    public ChatUtility chatUtility;

    // public RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
    @Override
    public void onEnable() {
        new Checker(this).getLatestVersion(latest -> {
            if (currentVersion.equalsIgnoreCase(latest)) {
                getLogger().info("You're using actual version.");
            } else if (currentVersion.contains("snap")) {
                getLogger().warning("You're using snapshot. It very unstable. If you meet bugs or errors with this plugin — made issue on Github:");
                getLogger().warning("https://github.com/LazyCat0/LazyChat-MC-plugin/issues");
            }
            else {
                getLogger().warning("Wait, you not updated a lot!");
                getLogger().warning("Current version: " + currentVersion);
                getLogger().warning("New version: " + latest);
                getLogger().warning("Download: https://github.com/LazyCat0/LazyChat-MC-plugin/releases");
            }
        });
        migrator();
        saveDefaultConfig();
        MuteManager muteManager = new MuteManager(this);
        muteManager.LoadMuteList();
        languageManager = new LanguageManager(this);
        languageManager.loadLanguages(this);

        instance = this;

        this.chatUtility = new ChatUtility(this);
        getServer().getPluginManager().registerEvents(new lcManager(this, muteManager ,languageManager), this);

        Objects.requireNonNull(getCommand("lazychat")).setExecutor(new LCCommand(this, languageManager));
        Objects.requireNonNull(getCommand("lazychat")).setTabCompleter(new LCCommandCompleter());
        Objects.requireNonNull(getCommand("broadcast")).setExecutor(new BCCommand(languageManager));
        Objects.requireNonNull(getCommand("l-mute")).setExecutor(new MuteCommand(muteManager, languageManager));
        Objects.requireNonNull(getCommand("l-mute")).setTabCompleter(new MuteCommandCompleter());
        getServer().getPluginManager().registerEvents(new SignListener(), this);
    }
    public ChatUtility getChatUtility() {
        return chatUtility;
    }

    private void migrator() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
            return;
        }
        reloadConfig();
        int currentVersion = getConfig().getInt("config-version", 1);
        int latestVersion = 5;

        if (currentVersion >= latestVersion) return;

        getLogger().info("Updating config from v" + currentVersion + " to v" + latestVersion);

        YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(configFile);

        YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(Objects.requireNonNull(getResource("config.yml")))
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
            reloadConfig();
            getLogger().info("Config successfully updated to version " + latestVersion);
        } catch (IOException e) {
            getLogger().severe("Could not save updated config: " + e.getMessage());
        }
    }
}
// by LazyCato0o