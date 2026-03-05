package lazy.dev.lazyChat;

import lazy.dev.lazyChat.chatSystem.ChatUtility;
import lazy.dev.lazyChat.chatSystem.lcManager;
import lazy.dev.lazyChat.commands.BCCommand;
import lazy.dev.lazyChat.commands.LCCommand;
import lazy.dev.lazyChat.commands.LCCommandCompleter;
import lazy.dev.lazyChat.commands.muteCommand.MuteCommand;
import lazy.dev.lazyChat.commands.muteCommand.MuteManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class LazyChat extends JavaPlugin {
    public static LazyChat instance;
    public LanguageManager languageManager;
    public ChatUtility chatUtility;
    private LuckPerms lp;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        MuteManager muteManager = new MuteManager(this);
        muteManager.LoadMuteList();
        languageManager = new LanguageManager(this);
        languageManager.loadLanguages(this);
        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        instance = this;

        if (provider != null) {
            this.lp = provider.getProvider();
        }

        this.chatUtility = new ChatUtility(this, lp);
        getServer().getPluginManager().registerEvents(new lcManager(this, muteManager ,languageManager), this);

        getCommand("lazychat").setExecutor(new LCCommand(this, languageManager));
        getCommand("lazychat").setTabCompleter(new LCCommandCompleter());
        getCommand("broadcast").setExecutor(new BCCommand(languageManager));
        getCommand("l-mute").setExecutor(new MuteCommand(muteManager, languageManager));
    }
    public ChatUtility getChatUtility() {
        return chatUtility;
    }
}
// by LazyCato0o