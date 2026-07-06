package dev.lazycat.lazyChat;

import dev.lazycat.lazyChat.api.language.LanguageManager;
import dev.lazycat.lazyChat.commands.BCCommand;
import dev.lazycat.lazyChat.commands.LCCommandKt;
import dev.lazycat.lazyChat.commands.muteCommand.MuteManager;
import dev.lazycat.lazyChat.Listeners.FormatListener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class LazyChat extends JavaPlugin {
    public String currentVersion = this.getPluginMeta().getVersion();
    public static LazyChat instance;
    public MuteManager muteManager;
    private LanguageManager lang;

    @Override
    public void onEnable() {
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
        muteManager = new MuteManager(this);
        muteManager.LoadMuteList();
        // I don't know...
        instance = this;
        // commands init
        Objects.requireNonNull(getCommand("lazychat")).setExecutor(new LCCommandKt(this));
        Objects.requireNonNull(getCommand("lazychat")).setTabCompleter(new LCCommandKt(this));
//
        Objects.requireNonNull(getCommand("lbroadcast")).setExecutor(new BCCommand(this));
//
//        Objects.requireNonNull(getCommand("l-mute")).setExecutor(new MuteCommand(muteManager));
//        Objects.requireNonNull(getCommand("l-mute")).setTabCompleter(new MuteCommandCompleter());

        // listeners
        getServer().getPluginManager().registerEvents(new FormatListener(), this);
        saveFiles();
    }
    public LanguageManager getLang() {
        return lang;
    }
    private void saveFiles() {
        saveResource("lang/English.yml", true);
        saveResource("lang/Russian.yml", true);
        saveResource("lang/Ukrainian.yml", true);
        saveResource("templates/admin.yml", false);
        saveResource("templates/business.json", false);
        saveResource("templates/global.yml", false);
        saveResource("templates/local.yml", false);
        saveResource("templates/whisper.yml", false);
    }
}