package lazy.dev.lazyChat.chatSystem;

import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
                plugin.getConfig().getString("global-chat-format", "<dark_gray>|<blue>L</blue>|</dark_gray> <prefix><reset><gold><nickname></gold>{s} <gray>>>><reset> <m>") :
                plugin.getConfig().getString("local-chat-format", "<dark_gray>|<green>G</green>|</dark_gray> <prefix><reset><gold><nickname></gold><suffix> <gray>>>><reset> <m>");

        return MiniMessage.miniMessage().deserialize(
                formatTemplate,
                Placeholder.parsed("nickname", player.getName()),
                Placeholder.parsed("m", message),
                Placeholder.parsed("prefix", prefix(player)),
                Placeholder.parsed("suffix", suffix(player))
        );
    }

    public boolean isGlobalChat(String message) {
        String prefix = plugin.getConfig().getString("prefix", "!");

        if (prefix.isEmpty()) {
            return true;
        }

        return message != null && message.startsWith(prefix);
    }

    public String getMessageContent(String originalMessage) {
        String prefix = plugin.getConfig().getString("prefix", "!");
        if (prefix.isEmpty()) {
            return originalMessage;
        }
        if (isGlobalChat(originalMessage)) {
            return originalMessage.substring(prefix.length()).trim();
        }

        return originalMessage;
    }

    public int getLocalChatRadius() {
        return plugin.getConfig().getInt("chat-radius", 100);
    }

    public boolean isConsoleLoggingEnabled() {
        return plugin.getConfig().getBoolean("enable-console-logging", true);
    }
}
// by LazyCato0o