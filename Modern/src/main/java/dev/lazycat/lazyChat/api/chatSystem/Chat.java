package dev.lazycat.lazyChat.api.chatSystem;

import dev.lazycat.lazyChat.api.chatSystem.configs.ChatTemplate;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public record Chat(String name, List<String> prefixes, int radius, String permission, ChatTemplate template) {
    public Chat(String name, List<String> prefixes, int radius, String permission, ChatTemplate template) {
        this.name = name;
        this.prefixes = prefixes != null ? prefixes : Collections.emptyList();
        this.radius = radius;
        this.permission = permission;
        this.template = template;
    }

    public boolean canUse(Player player) {
        return permission == null || permission.isEmpty() || player.hasPermission(permission);
    }
}