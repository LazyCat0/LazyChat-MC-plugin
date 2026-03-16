package lazy.dev.lazyChat.chatSystem;

import io.papermc.paper.event.player.AsyncChatEvent;
import lazy.dev.lazyChat.LanguageManager;
import lazy.dev.lazyChat.LazyChat;
import lazy.dev.lazyChat.commands.muteCommand.MuteManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class lcManager implements Listener {
    private final ChatUtility chatUtility;
    private final MuteManager muteManager;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final LanguageManager lang;

    public lcManager(LazyChat plugin, MuteManager muteManager, LanguageManager languageManager) {
        this.muteManager = muteManager;
        this.chatUtility = plugin.getChatUtility();
        this.lang = languageManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (muteManager.isMuted(player.getUniqueId())) {
            player.sendMessage(mm.deserialize(lang.getRaw("Mute_you_muted")));
            event.setCancelled(true);
            return;
        }

        Component originalMessage = event.message();
        String plainText = PlainTextComponentSerializer.plainText().serialize(originalMessage);

        boolean isGlobal = chatUtility.isGlobalChat(plainText);
        String messageContent = chatUtility.getMessageContent(plainText);

        event.setCancelled(true);

        String content = originalMessage.toString().substring(1).trim();

        if (content.isEmpty()) {
            player.sendMessage(lang.getFormated("Cannot send empty message"));
            event.setCancelled(true);
            return;
        }


        if (isGlobal) {
            sendGlobalMessage(player, messageContent, event);
        } else {
            sendLocalMessage(player, messageContent, event);
        }
    }

    private void sendLocalMessage(Player sender, String message, AsyncChatEvent e) {
        Component formattedMessage = chatUtility.formatMessage(sender, message, false);
        int radius = chatUtility.getLocalChatRadius();

        for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
            if (onlinePlayer.getWorld().equals(sender.getWorld())) {
                double distance = onlinePlayer.getLocation().distance(sender.getLocation());
                if (distance <= radius) {
                    if (message.contains("<newline>")) {
                        if (!sender.hasPermission("l-chat.full-format-use")) {
                            sender.sendMessage(lang.getFormated("CUNT cause DNHP"));
                            e.setCancelled(true);
                        }
                    }
                    onlinePlayer.sendMessage(formattedMessage);
                }
            }
        }

        if (chatUtility.isConsoleLoggingEnabled()) {
            sender.getServer().getConsoleSender().sendMessage(formattedMessage);
        }
    }

    private void sendGlobalMessage(Player sender, String message, AsyncChatEvent e) {
        Component formattedMessage = chatUtility.formatMessage(sender, message, true);

        for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
            if (message.contains("<newline>")) {
                if (!sender.hasPermission("l-chat.full-format-use")) {
                    sender.sendMessage(lang.getFormated("CUNT cause DNHP"));
                    e.setCancelled(true);
                }
            }
            onlinePlayer.sendMessage(formattedMessage);
        }

        if (chatUtility.isConsoleLoggingEnabled()) {
            sender.getServer().getConsoleSender().sendMessage(formattedMessage);
        }
    }
}
// by LazyCato0o