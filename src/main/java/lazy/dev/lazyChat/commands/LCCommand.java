package lazy.dev.lazyChat.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lazy.dev.lazyChat.LanguageManager;
import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class LCCommand implements CommandExecutor {
    private final LanguageManager lang;
    private final LazyChat plugin;
    MiniMessage mm = MiniMessage.miniMessage();
    public LCCommand(LazyChat plugin, LanguageManager lang) {
        this.plugin = plugin;
        this.lang = lang;
    }
    @Override
    public boolean onCommand(CommandSender source, Command command, String label, String[] args) {
        if (args.length == 0) {
            source.sendMessage(lang.get("LC_unknown_arg"));
            return true;
        }

        String act = args[0].toLowerCase();
        switch (act) {
            case "reload":
                if (!source.hasPermission("l-chat.reload")) {
                    source.sendMessage("LC_cannot_use");
                    return true;
                }
                try {
                    plugin.reloadPluginConfig();
                    source.sendMessage(lang.get("LC_reload"));
                } catch (Exception e) {
                    source.sendMessage(lang.get("LC_reload_error") + " " + e.getMessage());
                    plugin.getLogger().severe("Plugin has found error while reloading: " + e.getMessage());
                    return true;
                }
                break;
            case "info":
                source.sendMessage(lang.get("LC_info"));
                break;
            default:
                source.sendMessage(lang.get("LC_unknown_arg"));
                break;
        }
        return true;
    }
}
