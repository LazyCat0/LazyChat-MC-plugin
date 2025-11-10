package lazy.dev.lazyChat;

import lazy.dev.lazyChat.chatSystem.ChatUtility;
import lazy.dev.lazyChat.chatSystem.lcManager;
import lazy.dev.lazyChat.commands.LazyChatCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class LazyChat extends JavaPlugin {
    private static LazyChat instance;
    private ChatUtility chatManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.chatManager = new ChatUtility(this);

        getServer().getPluginManager().registerEvents(new lcManager(this), this);

        getCommand("l-chat-rl").setExecutor(new LazyChatCommand(this));

    }
    public ChatUtility getChatManager() {
        return chatManager;
    }
    public void reloadPluginConfig() {
        reloadConfig();
        chatManager.reloadConfig();
    }
}
