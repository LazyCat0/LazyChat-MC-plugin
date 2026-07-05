package dev.lazycat.lazyChat.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BCCommand implements CommandExecutor {
    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(CommandSender source, Command command, String label, String[] args) {
        if (args.length == 0) {
            return true;
        }
        String nonFormatedMessage = String.join(" ", args);
        for (Player Players : Bukkit.getOnlinePlayers()) {
        }
        return true;
    }
}
// by LazyCato0o