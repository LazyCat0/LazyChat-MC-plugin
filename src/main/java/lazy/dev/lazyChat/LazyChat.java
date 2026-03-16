package lazy.dev.lazyChat;

import lazy.dev.lazyChat.chatSystem.ChatUtility;
import lazy.dev.lazyChat.chatSystem.lcManager;
import lazy.dev.lazyChat.commands.BCCommand;
import lazy.dev.lazyChat.commands.LCCommand;
import lazy.dev.lazyChat.commands.LCCommandCompleter;
import lazy.dev.lazyChat.commands.sign.*;
import lazy.dev.lazyChat.commands.muteCommand.MuteCommand;
import lazy.dev.lazyChat.commands.muteCommand.MuteCommandCompleter;
import lazy.dev.lazyChat.commands.muteCommand.MuteManager;
// import net.luckperms.api.LuckPerms;
// import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LazyChat extends JavaPlugin {
    public static LazyChat instance;
    public LanguageManager languageManager;
    public ChatUtility chatUtility;

    // public RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);

    @Override
    public void onEnable() {
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
        Objects.requireNonNull(getCommand("sf")).setExecutor(new FormatCommand(this, languageManager));
        Objects.requireNonNull(getCommand("sf")).setTabCompleter(new FormatCommandCompleter());

        if (getConfig().getInt("config version") != 2) {
            this.saveResource("config.yml", true);
        }
    }
    public ChatUtility getChatUtility() {
        return chatUtility;
    }
}
// by LazyCato0o