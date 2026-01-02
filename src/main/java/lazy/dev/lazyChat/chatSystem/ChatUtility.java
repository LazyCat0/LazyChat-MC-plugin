package lazy.dev.lazyChat.chatSystem;

import lazy.dev.lazyChat.LazyChat;
import lazy.dev.lazyChat.LazyChatConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.entity.Player;

public class ChatUtility {
    private final LazyChatConfig config;
    private final LuckPerms lp;

    public ChatUtility(LazyChat plugin, LuckPerms lp) {
        this.config = new LazyChatConfig(plugin);
        this.lp = lp;
    }
    public void reloadConfig() {
        this.config.reload();
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

    public Component formatMessage(Player player, String message, boolean isGlobal) {
        String formatTemplate = isGlobal ?
                config.getGlobalChatFormat() :
                config.getLocalChatFormat();
        String formatted = formatTemplate
                .replace("{player}", player.getName())
                .replace("{message}", message)
                .replace("{prefix}", prefix(player));

        return MiniMessage.miniMessage().deserialize(formatted);
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
