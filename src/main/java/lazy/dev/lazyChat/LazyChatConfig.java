package lazy.dev.lazyChat;

import org.bukkit.configuration.file.FileConfiguration;

public class LazyChatConfig {
    private final LazyChat plugin;
    private FileConfiguration config;

    private int localChatRadius;
    private String globalChatPrefix;
    private String globalChatFormat;
    private String localChatFormat;
    private boolean enableConsoleLogging;

    public LazyChatConfig(LazyChat plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.config = plugin.getConfig();

        this.localChatRadius = config.getInt("local-chat-radius", 100);
        this.globalChatPrefix = config.getString("global-chat-prefix", "!");
        this.globalChatFormat = config.getString("global-chat-format",
                "<dark_gray>[<red>G</red>]</dark_gray> <gold>{player}</gold> <gray>>>></gray> <white>{message}</white>");
        this.localChatFormat = config.getString("local-chat-format",
                "<dark_gray>[<green>L</green>]</dark_gray> <gold>{player}</gold> <gray>>>></gray> <white>{message}</white>");
        this.enableConsoleLogging = config.getBoolean("enable-console-logging", true);
    }

    public int getLocalChatRadius() {
        return localChatRadius;
    }

    public String getGlobalChatPrefix() {
        return globalChatPrefix;
    }

    public String getGlobalChatFormat() {
        return globalChatFormat;
    }

    public String getLocalChatFormat() {
        return localChatFormat;
    }

    public boolean isEnableConsoleLogging() {
        return enableConsoleLogging;
    }
}
