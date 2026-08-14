package dev.lazycat.lazyChat.api.chatSystem;

import dev.lazycat.lazyChat.LazyChat;
import dev.lazycat.lazyChat.api.chatSystem.configs.BlacklistConfig;
import dev.lazycat.lazyChat.api.language.LanguageManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

public class ChatListener implements Listener {
    private final JavaPlugin plugin;
    private final LazyChat lazyChat;
    private final CManager chatManager;
    private final LanguageManager lang;
    private LuckPerms luckPerms = null;

    private static final Pattern TAG_PATTERN = Pattern.compile("<(/?)([a-zA-Z0-9_\\-]+)(:[^>]*)?>");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<(gradient|rainbow)(:[^>]*)?>", Pattern.CASE_INSENSITIVE);

    public ChatListener(LazyChat plugin, CManager chatManager) {
        this.plugin = plugin;
        this.lazyChat = plugin;
        this.chatManager = chatManager;
        this.lang = plugin.getLang();
        if (plugin.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            this.luckPerms = LuckPermsProvider.get();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (lazyChat.muteManager.isMuted(player.getUniqueId())) {
            player.sendMessage(lang.getFormated("mute.messages.you_muted"));
            event.setCancelled(true);
            return;
        }

        MiniMessage mm = MiniMessage.miniMessage();

        Component originalMessage = event.message();
        String plainText = PlainTextComponentSerializer.plainText().serialize(originalMessage);

        Chat chat = chatManager.getChatForMessage(plainText);
        if (chat == null) {
            chat = chatManager.getDefaultChat();
        }
        if (chat == null) {
            return;
        }

        if (!chat.canUse(player)) {
            player.sendMessage(lang.getFormated("other.not-enough-rights"));
            event.setCancelled(true);
            return;
        }

        String messageContent = plainText;
        for (String prefix : chat.prefixes()) {
            if (plainText.startsWith(prefix)) {
                messageContent = plainText.substring(prefix.length()).trim();
                break;
            }
        }
        if (messageContent.isEmpty()) {
            player.sendMessage(lang.getFormated("other.CSEM"));
            event.setCancelled(true);
            return;
        }

        BlacklistConfig blacklist = chat.template().blacklist();
        if (blacklist.isGlobal()) {
            blacklist = getGlobalBlacklist();
        }
        if (!isMessageAllowed(player, messageContent, blacklist)) {
            player.sendMessage(lang.getFormated("other.CUNT_cause_DNHP"));
            event.setCancelled(true);
            return;
        }

        String prompt = chat.template().prompt();
        Component formatted = mm.deserialize(prompt,
                Placeholder.parsed("m", messageContent),
                Placeholder.parsed("nickname", player.getName()),
                Placeholder.parsed("prefix", getPrefix(player)),
                Placeholder.parsed("suffix", getSuffix(player))
        );
        double cooldownSec = chat.cooldown();
        if (cooldownSec > 0) {
            if (!lazyChat.getChatCooldown().checkAndUpdate(player.getUniqueId(), cooldownSec)) {
                double remaining = lazyChat.getChatCooldown().getRemaining(player.getUniqueId(), cooldownSec);
                player.sendMessage(lang.getFormated("other.cooldown",
                        Placeholder.parsed("seconds", String.format("%.1f", remaining))
                ));
                event.setCancelled(true);
                return;
            }
        }

        int radius = chat.radius();
        if (radius == -1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(formatted);
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().equals(player.getWorld()) &&
                        p.getLocation().distance(player.getLocation()) <= radius) {
                    p.sendMessage(formatted);
                }
            }
        }

        if (plugin.getConfig().getBoolean("options.enable-logging", true)) {
            plugin.getServer().getConsoleSender().sendMessage(formatted);
        }

        event.setCancelled(true);
    }

    private BlacklistConfig getGlobalBlacklist() {
        BlacklistConfig global = new BlacklistConfig();
        global.setGlobal(true);
        global.setColors(plugin.getConfig().getBoolean("blacklist.chat.colors", false));
        global.setGradients(plugin.getConfig().getBoolean("blacklist.chat.gradients", false));
        global.setTags(plugin.getConfig().getStringList("blacklist.chat.tags"));
        return global;
    }

    private boolean isMessageAllowed(Player player, String message, BlacklistConfig blacklist) {
        if (blacklist.isColors()) {
            if (hasAnyTag(message)) {
                return false;
            }
        }

        if (blacklist.isGradients()) {
            if (hasGradientTag(message)) {
                return false;
            }
        }

        if (!player.hasPermission("l-chat.advance_format")) {
            for (String tag : blacklist.getTags()) {
                if (hasSpecificTag(message, tag)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean hasAnyTag(String message) {
        return TAG_PATTERN.matcher(message).find();
    }
    private boolean hasGradientTag(String message) {
        return GRADIENT_PATTERN.matcher(message).find();
    }
    private boolean hasSpecificTag(String message, String tagName) {
        Pattern pattern = Pattern.compile("<(/?)(?i:" + Pattern.quote(tagName) + ")(:[^>]*)?>");
        return pattern.matcher(message).find();
    }

    private String getPrefix(Player player) {
        if (luckPerms == null) return "";
        PlayerAdapter<Player> adapter = luckPerms.getPlayerAdapter(Player.class);
        CachedMetaData meta = adapter.getMetaData(player);
        String prefix = meta.getPrefix();
        return prefix != null ? prefix : "";
    }

    private String getSuffix(Player player) {
        if (luckPerms == null) return "";
        PlayerAdapter<Player> adapter = luckPerms.getPlayerAdapter(Player.class);
        CachedMetaData meta = adapter.getMetaData(player);
        String suffix = meta.getSuffix();
        return suffix != null ? suffix : "";
    }
}