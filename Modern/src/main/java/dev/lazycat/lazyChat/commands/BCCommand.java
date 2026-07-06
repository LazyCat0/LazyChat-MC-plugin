package dev.lazycat.lazyChat.commands;

import dev.lazycat.lazyChat.LazyChat;
import dev.lazycat.lazyChat.api.language.LanguageManager;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class BCCommand implements CommandExecutor {
    private final LazyChat plugin;
    private LuckPerms lp;
    public BCCommand(LazyChat plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager().isPluginEnabled("LuckPerms")) this.lp = LuckPermsProvider.get();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender source, @NonNull Command command, @NonNull String label, String[] args) {
        LanguageManager lang = plugin.getLang();
        if (args.length == 0) {
            source.sendMessage(lang.getFormated("broadcast.command.usage"));
            return true;
        }
        UUID uuid = plugin.getServer().getPlayerUniqueId(source.getName());
        if (plugin.muteManager.isMuted(uuid)) {
            source.sendMessage(lang.getFormated("mute.messages.you_muted"));
            return true;
        }
        String raw = String.join(" ", args);

        for (Player p : Bukkit.getOnlinePlayers()) {
           p.sendMessage(lang.getFormated("broadcast.messages.new-broadcast",
                   Placeholder.parsed("m", raw)));
           p.sendMessage(lang.getFormated("broadcast.messages.broadcast-by",
                   Placeholder.parsed("player", source.getName()),
                   Placeholder.parsed("prefix", prefix((Player) source)),
                   Placeholder.parsed("suffix", suffix((Player) source))
                   )
           );
        }
        return true;
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
}
// by LazyCato0o