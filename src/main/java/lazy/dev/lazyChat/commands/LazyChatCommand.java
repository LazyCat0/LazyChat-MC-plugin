package lazy.dev.lazyChat.commands;

import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LazyChatCommand implements CommandExecutor {
    private final LazyChat plugin;

    public LazyChatCommand(LazyChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("l-chat.reload")) {
            sender.sendMessage(Component.text("You can not use this command", NamedTextColor.RED));
            return true;
        }

        try {
            plugin.reloadPluginConfig();
            sender.sendMessage(Component.text("LazyChat config has reloaded!", NamedTextColor.GREEN));
        } catch (Exception e) {
            sender.sendMessage(Component.text("While reloading config it meets error: " + e.getMessage(), NamedTextColor.RED));
            plugin.getLogger().severe("Config reload error: " + e.getMessage());
        }

        return true;
    }
}
