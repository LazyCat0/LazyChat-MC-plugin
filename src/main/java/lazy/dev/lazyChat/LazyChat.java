package lazy.dev.lazyChat;

import org.bukkit.plugin.java.JavaPlugin;

public final class LazyChat extends JavaPlugin {
    private static LazyChat instance;
    private ChatManager chatManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.chatManager = new ChatManager(this);

        getServer().getPluginManager().registerEvents(new lazyChatSystem(this), this);

        this.getCommand("l-chat-rl").setExecutor(new lchatreload(this));

    }
    public static LazyChat getInstance() {
        return instance;
    }
    public ChatManager getChatManager() {
        return chatManager;
    }
    public void reloadPluginConfig() {
        reloadConfig();
        chatManager.reloadConfig();
    }
}
