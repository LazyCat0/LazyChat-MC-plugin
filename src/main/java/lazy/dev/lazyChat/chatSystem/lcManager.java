package lazy.dev.lazyChat.chatSystem;

import io.papermc.paper.event.player.AsyncChatEvent;
import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class lcManager implements Listener {
    private final LazyChat plugin;
    private final ChatUtility chatUtility;

    public lcManager(LazyChat plugin) {
        this.plugin = plugin;
        this.chatUtility = plugin.getChatManager();
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Component originalMessage = event.message();
        String plainText = PlainTextComponentSerializer.plainText().serialize(originalMessage);

        boolean isGlobal = chatUtility.isGlobalChat(plainText);
        String messageContent = chatUtility.getMessageContent(plainText);

        event.setCancelled(true);

        if (isGlobal) {
            sendGlobalMessage(player, messageContent);
        } else {
            sendLocalMessage(player, messageContent);
        }
    }

    private void sendLocalMessage(Player sender, String message) {
        Component formattedMessage = chatUtility.formatMessage(sender, message, false);
        int radius = chatUtility.getLocalChatRadius();

        for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
            if (onlinePlayer.getWorld().equals(sender.getWorld())) {
                double distance = onlinePlayer.getLocation().distance(sender.getLocation());
                if (distance <= radius) {
                    onlinePlayer.sendMessage(formattedMessage);
                }
            }
        }

        if (chatUtility.isConsoleLoggingEnabled()) {
            sender.getServer().getConsoleSender().sendMessage(formattedMessage);
        }
    }

    private void sendGlobalMessage(Player sender, String message) {
        Component formattedMessage = chatUtility.formatMessage(sender, message, true);

        for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(formattedMessage);
        }

        if (chatUtility.isConsoleLoggingEnabled()) {
            sender.getServer().getConsoleSender().sendMessage(formattedMessage);
        }
    }
}

