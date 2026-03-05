package lazy.dev.lazyChat.chatSystem;

import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatUtility {
    private final JavaPlugin plugin;
    private final LuckPerms lp;

    public ChatUtility(LazyChat plugin, LuckPerms lp) {
        this.plugin = plugin;
        this.lp = lp;
    }
    public void reloadConfig() {
        this.plugin.reloadConfig();
    }

    public String prefix(Player player) {
        if (lp == null) {
            return "";
        }
        PlayerAdapter<Player> adapter = lp.getPlayerAdapter(Player.class);
        CachedMetaData metaData = adapter.getMetaData(player);
        String prefix = metaData.getPrefix();
        return prefix != null ? prefix : "";
    }
    public String suffix(Player player) {
        if (lp == null) {
            return "";
        }
        PlayerAdapter<Player> adapter = lp.getPlayerAdapter(Player.class);
        CachedMetaData metaData = adapter.getMetaData(player);
        String suffix = metaData.getSuffix();
        return suffix != null ? suffix : "";
    }

    public Component formatMessage(Player player, String message, boolean isGlobal) {
        String formatTemplate = isGlobal ?
                plugin.getConfig().getString("global-chat-format", "<dark_gray>|<green>G</green>|</dark_gray> {prefix}<reset><gold>{player}</gold>{suffix} <gray>>>><reset> {message}") :
                plugin.getConfig().getString("local-chat-format", "<dark_gray>|<blue>L</blue>|</dark_gray> {prefix}<reset><gold>{player}</gold>{suffix} <gray>>>><reset> {message}");
        String formatted = formatTemplate
                .replace("{player}", player.getName())
                .replace("{message}", message)
                .replace("{prefix}", prefix(player))
                .replace("{suffix}", suffix(player));

        return MiniMessage.miniMessage().deserialize(formatted);
    }

    public boolean isGlobalChat(String message) {
        String prefix = plugin.getConfig().getString("global-chat-prefix", "");

        if (prefix.isEmpty()) {
            return true;
        }

        return message != null && message.startsWith(prefix);
    }

    public String getMessageContent(String originalMessage) {
        String prefix = plugin.getConfig().getString("global-chat-prefix", "");
        if (prefix.isEmpty()) {
            return originalMessage;
        }
        if (isGlobalChat(originalMessage)) {
            return originalMessage.substring(prefix.length()).trim();
        }

        return originalMessage;
    }

    public int getLocalChatRadius() {
        return plugin.getConfig().getInt("local-chat-radius");
    }

    public boolean isConsoleLoggingEnabled() {
        return plugin.getConfig().getBoolean("enable-console-logging", true);
    }
}
// by LazyCato0o