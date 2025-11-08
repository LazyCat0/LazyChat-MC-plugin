package lazy.dev.lazyChat;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class lazyChatSystem implements Listener {
    private final LazyChat plugin;
    private final ChatManager chatManager;

    public lazyChatSystem(LazyChat plugin) {
        this.plugin = plugin;
        this.chatManager = plugin.getChatManager();
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Component originalMessage = event.message();
        String plainText = PlainTextComponentSerializer.plainText().serialize(originalMessage);

        boolean isGlobal = chatManager.isGlobalChat(plainText);
        String messageContent = chatManager.getMessageContent(plainText);

        event.setCancelled(true);

        if (isGlobal) {
            sendGlobalMessage(player, messageContent);
        } else {
            sendLocalMessage(player, messageContent);
        }
    }

    private void sendLocalMessage(Player sender, String message) {
        Component formattedMessage = chatManager.formatMessage(sender, message, false);
        int radius = chatManager.getLocalChatRadius();

        for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
            if (onlinePlayer.getWorld().equals(sender.getWorld())) {
                double distance = onlinePlayer.getLocation().distance(sender.getLocation());
                if (distance <= radius) {
                    onlinePlayer.sendMessage(formattedMessage);
                }
            }
        }

        if (chatManager.isConsoleLoggingEnabled()) {
            sender.getServer().getConsoleSender().sendMessage(formattedMessage);
        }
    }

    private void sendGlobalMessage(Player sender, String message) {
        Component formattedMessage = chatManager.formatMessage(sender, message, true);

        for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(formattedMessage);
        }

        if (chatManager.isConsoleLoggingEnabled()) {
            sender.getServer().getConsoleSender().sendMessage(formattedMessage);
        }
    }
}

