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
        Component formatedMessage = mm.deserialize("\n" + lang.getNonFormated("BC_new_broadcast") + "\n\n" + nonFormatedMessage + "<reset>\n\n" + lang.getNonFormated("BC_broadcast_by") + " " + source.getName() + "<reset>\n");
        for (Player Players : Bukkit.getOnlinePlayers()) {
            Players.sendMessage(formatedMessage);
        }
        source.getServer().getConsoleSender().sendMessage(formatedMessage);
        return true;
    }
}
