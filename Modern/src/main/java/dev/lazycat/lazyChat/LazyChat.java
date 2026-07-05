package dev.lazycat.lazyChat;

import dev.lazycat.lazyChat.commands.BCCommand;
import dev.lazycat.lazyChat.commands.LCCommandKt;
import dev.lazycat.lazyChat.commands.muteCommand.MuteCommand;
import dev.lazycat.lazyChat.commands.muteCommand.MuteCommandCompleter;
import dev.lazycat.lazyChat.commands.muteCommand.MuteManager;
import dev.lazycat.lazyChat.Listeners.FormatListener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class LazyChat extends JavaPlugin {
    public String currentVersion = this.getPluginMeta().getVersion();
    public static LazyChat instance;
    public MuteManager muteManager;

    @Override
    public void onEnable() {
        getLogger().info("    @      @@@  @@@@@ @   @  @@@  @   @  @@@  @@@@@            @   @  @@@  @@@@  @@@@@ @@@@  @   @ \n" +
                "   @     @   @    @   @ @  @     @   @ @   @   @              @@ @@ @   @ @   @ @     @   @ @@  @  \n" +
                "  @     @@@@@   @     @   @     @@@@@ @@@@@   @      @@@@    @ @ @ @   @ @   @ @@@@  @@@@  @ @ @   \n" +
                " @     @   @  @      @   @     @   @ @   @   @              @   @ @   @ @   @ @     @  @  @  @@    \n" +
                "@@@@@ @   @ @@@@@   @    @@@  @   @ @   @   @              @   @  @@@  @@@@  @@@@@ @   @ @   @     ");
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
        LazyChatA.migrator(this);
        saveDefaultConfig();
        muteManager = new MuteManager(this);
        muteManager.LoadMuteList();

        instance = this;

//        Objects.requireNonNull(getCommand("lazychat")).setExecutor(new LCCommandKt(this, languageManager));
//        Objects.requireNonNull(getCommand("lazychat")).setTabCompleter(new LCCommandKt(this, languageManager));
//
//        Objects.requireNonNull(getCommand("lbroadcast")).setExecutor(new BCCommand(languageManager));
//
//        Objects.requireNonNull(getCommand("l-mute")).setExecutor(new MuteCommand(muteManager, languageManager));
//        Objects.requireNonNull(getCommand("l-mute")).setTabCompleter(new MuteCommandCompleter());

        getServer().getPluginManager().registerEvents(new FormatListener(), this);
    }
}