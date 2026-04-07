package lazy.dev.lazyChat.commands.muteCommand;

import lazy.dev.lazyChat.LanguageManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MuteCommand implements CommandExecutor {
    private final MuteManager muteManager;
    private final LanguageManager lang;

    private long parseTime(long amount, String unit) {
        return switch (unit.toLowerCase()) {
            case "minutes", "m", "min" -> amount * 60 * 1000;
            case "hours", "h", "hour"  -> amount * 60 * 60 * 1000;
            case "days", "d", "day"    -> amount * 24 * 60 * 60 * 1000;
            default -> -1;
        };
    }

    public MuteCommand(MuteManager muteManager, LanguageManager languageManager) {
        this.muteManager = muteManager;
        this.lang = languageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
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
            sender.sendMessage(MiniMessage.miniMessage().deserialize(target.getName() + " " + lang.getRaw("Mute_player_unmute")));
            target.getPlayer().sendMessage(lang.getFormated("Mute_you_unmuted"));
        }
        if (action.equals("mute")) {
            if (args.length < 4) {
                sender.sendMessage(lang.getFormated("Mute_set_time"));
                return true;
            }
            try {
                long amount = Long.parseLong(args[2]);
                String unit = args[3];
                long duration = parseTime(amount, unit);

                if (duration <= 0) {
                    sender.sendMessage(lang.getFormated("Mute_unknown_time_format"));
                    return true;
                }

                muteManager.mute(target.getUniqueId(), duration);

                String timeStr = amount + " " + unit;
                sender.sendMessage(MiniMessage.miniMessage().deserialize(target.getName() + " " + lang.getRaw("Mute_player_muted") + " " + timeStr));

                if (target.isOnline() && target.getPlayer() != null) {
                    target.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(lang.getRaw("Mute_you_just_muted") + " " + timeStr));
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(lang.getFormated("Mute_unknown_time_format"));
            }
        }
        return true;
    }
}
// By LazyCato0o