package lazy.dev.lazyChat.commands;

import lazy.dev.lazyChat.LanguageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BCCommand implements CommandExecutor {
    private final LanguageManager lang;
    MiniMessage mm = MiniMessage.miniMessage();
    public BCCommand(LanguageManager lang) {
        this.lang = lang;
    }

    @Override
    public boolean onCommand(CommandSender source, Command command, String label, String[] args) {
        if (!source.hasPermission("l-chat.broadcast")) {
            source.sendMessage(lang.get("LC_cannot_use"));
            return true;
        }
        if (args.length == 0) {
            source.sendMessage(lang.get("BC_usage"));
            return true;
        }
        String nonFormatedMessage = String.join(" ", args);
        Component formatedMessage = mm.deserialize( lang.getNonFormated("BC_new_broadcast") + "\n\n" + nonFormatedMessage + "\n\n" + lang.getNonFormated("BC_broadcast_by") + source.getName() + "<reset>");
        Player Players = (Player) Bukkit.getOnlinePlayers();
        Players.sendMessage(formatedMessage);
        return true;
    }
}
