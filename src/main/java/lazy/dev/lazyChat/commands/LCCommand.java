package lazy.dev.lazyChat.commands;

import lazy.dev.lazyChat.LanguageManager;
import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LCCommand implements CommandExecutor {
    private final LanguageManager lang;
    private final LazyChat plugin;
    MiniMessage mm = MiniMessage.miniMessage();
    public LCCommand(LazyChat plugin, LanguageManager lang) {
        this.plugin = plugin;
        this.lang = lang;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender source, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            source.sendMessage(lang.getFormated("LC_unknown_arg"));
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
                    plugin.reloadConfig();
                    plugin.chatUtility.reloadConfig();
                    plugin.languageManager.loadLanguages(plugin);
                    source.sendMessage(lang.getFormated("LC_reload"));
                } catch (Exception e) {
                    source.sendMessage(lang.getFormated("LC_reload_error") + " " + e.getMessage());
                    plugin.getLogger().severe("Plugin has found error while reloading: " + e.getMessage());
                    return true;
                }
                break;
            case "info":
                source.sendMessage(lang.getFormated("LC_info"));
                break;
            default:
                source.sendMessage(lang.getFormated("LC_unknown_arg"));
                break;
        }
        return true;
    }
}
// by LazyCato0o