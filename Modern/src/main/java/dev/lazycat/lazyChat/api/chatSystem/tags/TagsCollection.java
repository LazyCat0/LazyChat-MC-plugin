package dev.lazycat.lazyChat.api.chatSystem.tags;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.entity.Player;

public class TagsCollection {
    private static LuckPerms luckPerms;
    public static TagResolver createPlayerResolver(Player p) {
        return TagResolver.resolver("nickname", (args, ctx) -> {
            Component playerComp = Component.text(p.getName());
            return Tag.selfClosingInserting(playerComp);
        });
    }
    public static TagResolver createPlayerPrefixResolver(Player p) {
        return TagResolver.resolver("prefix", (args, ctx) -> {
            Component playerComp = Component.text(getPrefix(p));
            return Tag.selfClosingInserting(playerComp);
        });
    }
    public static TagResolver createPlayerSuffixResolver(Player p) {
        return TagResolver.resolver("suffix", (args, ctx) -> {
            Component playerComp = Component.text(getSuffix(p));
            return Tag.selfClosingInserting(playerComp);
        });
    }


    private static String getPrefix(Player player) {
        if (luckPerms == null) return "";
        PlayerAdapter<Player> adapter = luckPerms.getPlayerAdapter(Player.class);
        CachedMetaData meta = adapter.getMetaData(player);
        String prefix = meta.getPrefix();
        return prefix != null ? prefix : "";
    }

    private static String getSuffix(Player player) {
        if (luckPerms == null) return "";
        PlayerAdapter<Player> adapter = luckPerms.getPlayerAdapter(Player.class);
        CachedMetaData meta = adapter.getMetaData(player);
        String suffix = meta.getSuffix();
        return suffix != null ? suffix : "";
    }
}
