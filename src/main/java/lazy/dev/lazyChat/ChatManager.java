package lazy.dev.lazyChat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatManager {
    private final LazyChatConfig config;
    private final LazyChat plugin;
    private LuckPerms luckPerms;
    private boolean luckPermsEnabled = false;

    public ChatManager(LazyChat plugin) {
        this.config = new LazyChatConfig(plugin);
        this.plugin = plugin;
        setupLuckPerms();
    }

    public void reloadConfig() {
        this.config.reload();
    }

    private void setupLuckPerms() {
        try {
            if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
                luckPerms = Bukkit.getServicesManager().load(LuckPerms.class);
                luckPermsEnabled = true;
                plugin.getLogger().info("LazyChat has found LP.");
            } else {
                plugin.getLogger().info("LazyChat can't found LP, by this reason prefixes doesn't work");
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error while loading LazyChat with LP: " + e.getMessage());
            luckPermsEnabled = false;
        }
    }

    public Component formatMessage(Player player, String message, boolean isGlobal) {
        String formatTemplate = isGlobal ?
                config.getGlobalChatFormat() :
                config.getLocalChatFormat();

        String playerPrefix = getPlayerPrefix(player);

        String formatted = formatTemplate
                .replace("{player}", player.getName())
                .replace("{message}", message)
                .replace("{prefix}", playerPrefix);

        return MiniMessage.miniMessage().deserialize(formatted);
    }

    private String getPlayerPrefix(Player player) {
        if (!luckPermsEnabled || luckPerms == null) {
            return "";
        }

        try {
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            if (user == null) {
                return "";
            }

            CachedMetaData metaData = user.getCachedData().getMetaData();
            String prefix = metaData.getPrefix();

            return prefix != null ? prefix : "";

        } catch (Exception e) {
            plugin.getLogger().warning("Can't get prefix of " + player.getName() + ": " + e.getMessage());
            return "";
        }
    }


    public boolean isGlobalChat(String message) {
        return message.startsWith(config.getGlobalChatPrefix());
    }

    public String getMessageContent(String originalMessage) {
        if (isGlobalChat(originalMessage)) {
            return originalMessage.substring(config.getGlobalChatPrefix().length()).trim();
        }
        return originalMessage;
    }

    public int getLocalChatRadius() {
        return config.getLocalChatRadius();
    }

    public boolean isConsoleLoggingEnabled() {
        return config.isEnableConsoleLogging();
    }
}
