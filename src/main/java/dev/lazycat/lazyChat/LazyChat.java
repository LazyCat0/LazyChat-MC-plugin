package dev.lazycat.lazyChat;

import dev.lazycat.lazyChat.api.chatSystem.*;
import dev.lazycat.lazyChat.api.language.LanguageManager;
import dev.lazycat.lazyChat.api.mute.MuteManager;
import dev.lazycat.lazyChat.commands.BCCommand;
import dev.lazycat.lazyChat.commands.LCCommandKt;
import dev.lazycat.lazyChat.commands.brigadier.MeCommand;
import dev.lazycat.lazyChat.commands.brigadier.TitleCommand;
import dev.lazycat.lazyChat.commands.muteCommand.MuteCommand;
import dev.lazycat.lazyChat.commands.muteCommand.MuteCommandCompleter;
import dev.lazycat.lazyChat.Listeners.FormatListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LazyChat extends JavaPlugin {
    public String currentVersion = this.getPluginMeta().getVersion();
    public static LazyChat instance;
    public MuteManager muteManager;
    private LanguageManager lang;
    private CManager chatManager;
    private CooldownThings chatCooldown;
    private CooldownThings broadcastCooldown;

    @Override
    public void onEnable() {
        sendLog();
        saveFiles();
        //version
        new Checker(this).getLatestVersion(latest -> {
            if (currentVersion.equalsIgnoreCase(latest)) {
                getLogger().info("You're using actual version.");
                if (currentVersion.contains("patch")) {
                    getLogger().info("I really made PATCH for my plugin? wow.");
                }
            } else if (currentVersion.contains("snap")) {
                getLogger().warning("You're using snapshot. It very unstable. If you meet bugs or errors with this plugin — made issue on Github:");
                getLogger().warning("https://github.com/LazyCat0/LazyChat-MC-plugin/issues");
            }
            else {
                getLogger().warning("Wait, you not updated a lot!");
                getLogger().warning("Current version: " + currentVersion);
                getLogger().warning("New version: " + latest);
                getLogger().warning("Download: https://modrinth.com/plugin/lazychat/versions");
            }
        });
        //language
        lang = new LanguageManager(this); // My experiment with .json and MM translator not success...
        // save some files
        saveDefaultConfig();
        // Mute :P
        boolean useDatabaseForMutes = getConfig().getBoolean("options.experiments.databasesForMute", false);
        muteManager = new MuteManager(this, useDatabaseForMutes);
        // I forget for what it...
        instance = this;

        // commands init
        Objects.requireNonNull(getCommand("lazychat")).setExecutor(new LCCommandKt(this));
        Objects.requireNonNull(getCommand("lazychat")).setTabCompleter(new LCCommandKt(this));
        Objects.requireNonNull(getCommand("lbroadcast")).setExecutor(new BCCommand(this));
        Objects.requireNonNull(getCommand("l-mute")).setExecutor(new MuteCommand(this));
        Objects.requireNonNull(getCommand("l-mute")).setTabCompleter(new MuteCommandCompleter());

        if (getConfig().getBoolean("options.experiments.replacements")) {
            this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, e -> {
                var registrar = e.registrar();
                if (getConfig().getBoolean("replacements.title", true)) {
                    registrar.register("title", new TitleCommand(this));
                }
                if (getConfig().getBoolean("replacements.me", true)) {
                    registrar.register("me", new MeCommand(this));
                }

            });
        }

        // listeners
        getServer().getPluginManager().registerEvents(new FormatListener(), this);

        // And finally, CHAT!
        chatManager = new CManager(this);
        getServer().getPluginManager().registerEvents(new ChatListener(this,chatManager), this);
        // cooldowns, hehe
        chatCooldown = new CooldownThings();
        broadcastCooldown = new CooldownThings();
    }
    public LanguageManager getLang() {
        return lang;
    }
    public CManager getChatManager() {
        return chatManager;
    }

    public CooldownThings getChatCooldown() {
        return chatCooldown;
    }
    public CooldownThings getBroadcastCooldown() {
        return broadcastCooldown;
    }

    private void saveFiles() {
        saveResource("lang/English.yml", true);
        saveResource("lang/Russian.yml", true);
        saveResource("lang/Ukrainian.yml", true);
        if (getConfig().getBoolean("options.save-internal-chats")) {
            saveResource("templates/admin.yml", false);
            saveResource("templates/business.json", false);
            saveResource("templates/global.yml", false);
            saveResource("templates/local.yml", false);
            saveResource("templates/whisper.yml", false);
        }
    }
    private void sendLog() {
        String asciiArt = """
            ##         #####    #########  ##      ##    ######   ##      ##   #####    #########
            ##        ##   ##          ##  ##      ##   ##    ##  ##      ##  ##   ##       ##    
            ##       ##     ##        ##    ##    ##    ##        ##      ## ##     ##      ##    
            ##       #########       ##      ##  ##     ##        ########## #########      ##    
            ##       ##     ##      ##        ####      ##        ##      ## ##     ##      ##    
            ##       ##     ##     ##          ##       ##    ##  ##      ## ##     ##      ##    
            ##       ##     ##    ##           ##        #    #   ##      ## ##     ##      ##    
            ######## ##     ##    #########    ##        ######   ##      ## ##     ##      ##    
            """;
        for (String line : asciiArt.split("\n")) {
            getServer().getConsoleSender().sendMessage(line);
        }
    }

    @Override
    public void onDisable() {
        if (muteManager != null) {
            muteManager.close();
        }
    }
}