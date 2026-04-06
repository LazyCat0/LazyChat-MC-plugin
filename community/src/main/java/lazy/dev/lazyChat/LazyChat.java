package lazy.dev.lazyChat;

import lazy.dev.lazyChat.chatSystem.ChatUtility;
import lazy.dev.lazyChat.chatSystem.lcManager;
import lazy.dev.lazyChat.commands.BCCommand;
import lazy.dev.lazyChat.commands.LCCommand;
import lazy.dev.lazyChat.commands.LCCommandCompleter;
import lazy.dev.lazyChat.commands.muteCommand.MuteCommand;
import lazy.dev.lazyChat.commands.muteCommand.MuteCommandCompleter;
import lazy.dev.lazyChat.commands.muteCommand.MuteManager;
// import net.luckperms.api.LuckPerms;
// import org.bukkit.plugin.RegisteredServiceProvider;
import lazy.dev.lazyChat.sign.SignListener;
import org.bukkit.plugin.java.JavaPlugin;

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

        if (getConfig().getInt("config-version") != 3) {
            getLogger().severe("Found config version that isn't compares with plugin version.");
            this.saveResource("config.yml", true);
        }
    }
    public ChatUtility getChatUtility() {
        return chatUtility;
    }
}
// by LazyCato0o