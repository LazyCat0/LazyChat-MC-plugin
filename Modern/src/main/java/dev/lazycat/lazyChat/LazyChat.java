package dev.lazycat.lazyChat;

import dev.lazycat.lazyChat.api.language.LC_MiniMessageTranslator;
import dev.lazycat.lazyChat.api.language.LangHelper;
import dev.lazycat.lazyChat.api.language.LanguageManager;
import dev.lazycat.lazyChat.commands.BCCommand;
import dev.lazycat.lazyChat.commands.LCCommandKt;
import dev.lazycat.lazyChat.commands.muteCommand.MuteCommand;
import dev.lazycat.lazyChat.commands.muteCommand.MuteCommandCompleter;
import dev.lazycat.lazyChat.commands.muteCommand.MuteManager;
import dev.lazycat.lazyChat.Listeners.FormatListener;
import net.kyori.adventure.translation.GlobalTranslator;
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
        LanguageManager lang = new LanguageManager(getDataPath());
        LC_MiniMessageTranslator translator = new LC_MiniMessageTranslator(lang);
        GlobalTranslator.translator().addSource(translator);
        LangHelper langHelper = new LangHelper(translator);
        // save some files
        saveDefaultConfig();
        muteManager = new MuteManager(this);
        muteManager.LoadMuteList();
        // i dont know...
        instance = this;
        // commands init
        Objects.requireNonNull(getCommand("lazychat")).setExecutor(new LCCommandKt(this, langHelper));
        Objects.requireNonNull(getCommand("lazychat")).setTabCompleter(new LCCommandKt(this, langHelper));
//
//        Objects.requireNonNull(getCommand("lbroadcast")).setExecutor(new BCCommand(languageManager));
//
//        Objects.requireNonNull(getCommand("l-mute")).setExecutor(new MuteCommand(muteManager, languageManager));
//        Objects.requireNonNull(getCommand("l-mute")).setTabCompleter(new MuteCommandCompleter());

        // listeners
        getServer().getPluginManager().registerEvents(new FormatListener(), this);
    }
}