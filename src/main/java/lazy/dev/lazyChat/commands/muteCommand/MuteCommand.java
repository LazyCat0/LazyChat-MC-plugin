package lazy.dev.lazyChat.commands.muteCommand;

import lazy.dev.lazyChat.LanguageManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

public class MuteCommand implements CommandExecutor {
    private final MuteManager muteManager;
    private final LanguageManager lang;

    private long parseTime(String input) {
        try {
            input = input.toLowerCase().trim();

            if (input.endsWith("m")) {
                return Long.parseLong(input.replace("m", "")) * 60 * 1000;
            } else if (input.endsWith("h")) {
                return Long.parseLong(input.replace("h", "")) * 60 * 60 * 1000;
            } else if (input.endsWith("d")) {
                return Long.parseLong(input.replace("d", "")) * 24 * 60 * 60 * 1000;
            } else {
                return Long.parseLong(input) * 60 * 1000;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public MuteCommand(MuteManager muteManager, LanguageManager languageManager) {
        this.muteManager = muteManager;
        this.lang = languageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length == 0) {
            sender.sendMessage(lang.getFormated("Mute_usage"));
            return true;
        }
        String action = args[0].toLowerCase();
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (target == null) {
            sender.sendMessage(lang.getFormated("Mute_usage"));
            return true;
        }

        if (action.equals("unmute")) {
            muteManager.unmute(target.getUniqueId());
            sender.sendMessage(MiniMessage.miniMessage().deserialize(target.getName() + " " + lang.getFormated("Mute_player_unmute")));
            target.getPlayer().sendMessage(lang.getFormated("Mute_you_unmuted"));
        }
        if (action.equals("mute")) {
            if (args.length < 3) {
                sender.sendMessage(lang.getFormated("Mute_set_time"));
                return true;
            }
            long duration = parseTime(args[2]);
            if (duration <= 0) {
                sender.sendMessage(lang.getFormated("Mute_unknown_time_format"));
                return true;
            }
            muteManager.mute(target.getUniqueId(), duration);
            sender.sendMessage(MiniMessage.miniMessage().deserialize(target.getName() + " " + lang.getRaw("Mute_player_muted") + " " + args[2]));
            target.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(lang.getRaw("Mute_you_muted") + args[2]));
        }
        return true;
    }
}
// By LazyCato0o