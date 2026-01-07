package lazy.dev.lazyChat;

import lazy.dev.lazyChat.chatSystem.ChatUtility;
import lazy.dev.lazyChat.chatSystem.lcManager;
import lazy.dev.lazyChat.commands.BCCommand;
import lazy.dev.lazyChat.commands.LCCommand;
import lazy.dev.lazyChat.commands.LCCommandCompleter;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class LazyChat extends JavaPlugin {
    public static LazyChat instance;
    private ChatUtility chatUtility;
    private LuckPerms lp;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.loadLanguages(this);
        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        instance = this;

        if (provider != null) {
            this.lp = provider.getProvider();
        }

        this.chatUtility = new ChatUtility(this, lp);
        getServer().getPluginManager().registerEvents(new lcManager(this), this);

        getCommand("lazychat").setExecutor(new LCCommand(this, languageManager));
        getCommand("lazychat").setTabCompleter(new LCCommandCompleter());
        getCommand("broadcast").setExecutor(new BCCommand(languageManager));
    }
    public ChatUtility getChatUtility() {
        return chatUtility;
    }
    public void reloadPluginConfig() {
        reloadConfig();
        chatUtility.reloadConfig();
    }
}
