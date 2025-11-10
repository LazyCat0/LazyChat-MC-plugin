package lazy.dev.lazyChat.chatSystem;

import lazy.dev.lazyChat.LazyChat;
import lazy.dev.lazyChat.LazyChatConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class ChatUtility {
    private final LazyChatConfig config;


    public ChatUtility(LazyChat plugin) {
        this.config = new LazyChatConfig(plugin);
    }

    public void reloadConfig() {
        this.config.reload();
    }

    public Component formatMessage(Player player, String message, boolean isGlobal) {
        String formatTemplate = isGlobal ?
                config.getGlobalChatFormat() :
                config.getLocalChatFormat();


        String formatted = formatTemplate
                .replace("{player}", player.getName())
                .replace("{message}", message)
                ;

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
