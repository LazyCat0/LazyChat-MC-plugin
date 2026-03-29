package lazy.dev.lazyChat;

import lazy.dev.lata.File.LataFile;
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

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public final class LazyChat extends JavaPlugin {
    public static LazyChat instance;
    public LanguageManager languageManager;
    public ChatUtility chatUtility;

    private final File file = new File(getDataFolder(), "config.lata");
    private LataFile config;

    // public RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);

    @Override
    public void onLoad() {
        saveResource("config.lata", false);
    }
    @Override
    public void onEnable() {
        MuteManager muteManager = new MuteManager(this);
        muteManager.LoadMuteList();
        languageManager = new LanguageManager(this);
        languageManager.loadLanguages(this);

        instance = this;

        try {
            config.load(file);
            Logger.getLogger("LC").info("Lata config loaded");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.chatUtility = new ChatUtility(this);
        getServer().getPluginManager().registerEvents(new lcManager(this, muteManager ,languageManager), this);

        Objects.requireNonNull(getCommand("lazychat")).setExecutor(new LCCommand(this, languageManager));
        Objects.requireNonNull(getCommand("lazychat")).setTabCompleter(new LCCommandCompleter());
        Objects.requireNonNull(getCommand("broadcast")).setExecutor(new BCCommand(languageManager));
        Objects.requireNonNull(getCommand("l-mute")).setExecutor(new MuteCommand(muteManager, languageManager));
        Objects.requireNonNull(getCommand("l-mute")).setTabCompleter(new MuteCommandCompleter());
        Objects.requireNonNull(getCommand("sf")).setExecutor(new FormatCommand(this, languageManager));
        Objects.requireNonNull(getCommand("sf")).setTabCompleter(new FormatCommandCompleter());

        if (!config.get("meta", "inst-version").equals(3)) {
            getLogger().severe("Found inst version that isn't compares with plugin version.");
            this.saveResource("config.lata", true);
        }
    }
    public ChatUtility getChatUtility() {
        return chatUtility;
    }
    public LataFile getLataConfig() {
        return config;
    }
    public void reloadLataConfig() throws IOException {
        config.load(file);
    }
}
// by LazyCato0o