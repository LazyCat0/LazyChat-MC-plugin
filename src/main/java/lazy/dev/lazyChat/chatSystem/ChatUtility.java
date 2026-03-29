package lazy.dev.lazyChat.chatSystem;

import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class ChatUtility {
    private final LazyChat plugin;
    private LuckPerms lp;

    public ChatUtility(LazyChat plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager().isPluginEnabled("LuckPerms")) this.lp = LuckPermsProvider.get();
    }
    public void reloadConfig() {
        this.plugin.reloadConfig();
    }

    public String prefix(Player player) {
        if (lp == null) {
            return "";
        }
        @Nullable PlayerAdapter<Player> adapter = lp.getPlayerAdapter(Player.class);
        @Nullable CachedMetaData metaData = adapter.getMetaData(player);
        @Nullable String prefix = metaData.getPrefix();
        return prefix != null ? prefix : "";
    }
    public String suffix(Player player) {
        if (lp == null) {
            return "";
        }
        @Nullable PlayerAdapter<Player> adapter = lp.getPlayerAdapter(Player.class);
        @Nullable CachedMetaData metaData = adapter.getMetaData(player);
        @Nullable String suffix = metaData.getSuffix();
        return suffix != null ? suffix : "";
    }

    public Component formatMessage(Player player, String message, boolean isGlobal) {
        String formatTemplate = isGlobal ?
                plugin.getLataConfig().get("messages", "global-chat-format").toString() :
                plugin.getLataConfig().get("messages", "local-chat-format").toString();
        String formatted = formatTemplate
                .replace("{p}", player.getName())
                .replace("{m}", message)
                .replace("{pr}", prefix(player))
                .replace("{s}", suffix(player));

        return MiniMessage.miniMessage().deserialize(formatted);
    }

    public boolean isGlobalChat(String message) {
        String prefix = plugin.getLataConfig().get("settings", "prefix").toString();

        if (prefix.isEmpty()) {
            return true;
        }

        return message != null && message.startsWith(prefix);
    }

    public String getMessageContent(String originalMessage) {
        String prefix = plugin.getLataConfig().get("messages", "global-chat-format").toString();
        if (prefix.isEmpty()) {
            return originalMessage;
        }
        if (isGlobalChat(originalMessage)) {
            return originalMessage.substring(prefix.length()).trim();
        }

        return originalMessage;
    }

    public int getLocalChatRadius() {
        return (int) plugin.getLataConfig().get("settings","local-chat-radius");
    }

    public boolean isConsoleLoggingEnabled() {
        return (boolean) plugin.getLataConfig().get("settings", "enable-console-logging");
    }
}
// by LazyCato0o