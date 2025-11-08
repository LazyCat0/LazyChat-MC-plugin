package lazy.dev.lazyChat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class lchatreload implements CommandExecutor {
    private final LazyChat plugin;

    public lchatreload(LazyChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("lazychat.reload")) {
            sender.sendMessage(ChatColor.RED + "You can not use this command");
            return true;
        }

        try {
            plugin.reloadPluginConfig();
            sender.sendMessage(ChatColor.GREEN + "LazyChat config has reloaded!");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "While reloading config it meets error: " + e.getMessage());
            plugin.getLogger().severe("Config reload error: " + e.getMessage());
        }

        return true;
    }
}
