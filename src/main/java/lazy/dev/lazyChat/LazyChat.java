package lazy.dev.lazyChat;

import lazy.dev.lazyChat.chatSystem.ChatUtility;
import lazy.dev.lazyChat.chatSystem.lcManager;
import lazy.dev.lazyChat.commands.BCCommand;
import lazy.dev.lazyChat.commands.LCCommand;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class LazyChat extends JavaPlugin {
    public static LazyChat instance;
    private ChatUtility chatUtility;
    private LuckPerms lp;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            this.lp = provider.getProvider();
        }
        new LCCommand(this).register();
        new BCCommand(this).register();
        this.chatUtility = new ChatUtility(this, lp);
        getServer().getPluginManager().registerEvents(new lcManager(this), this);
    }
    public ChatUtility getChatUtility() {
        return chatUtility;
    }
    public void reloadPluginConfig() {
        reloadConfig();
        chatUtility.reloadConfig();
    }
}
