package dev.lazycat.lazyChat.commands.muteCommand;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MuteCommand implements CommandExecutor {
    private final MuteManager muteManager;

    private long parseTime(long amount, String unit) {
        return switch (unit.toLowerCase()) {
            case "minutes", "m", "min" -> amount * 60 * 1000;
            case "hours", "h", "hour"  -> amount * 60 * 60 * 1000;
            case "days", "d", "day"    -> amount * 24 * 60 * 60 * 1000;
            default -> -1;
        };
    }

    public MuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) {
            return true;
        }
        String action = args[0].toLowerCase();
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (target == null) {
            return true;
        }

        if (action.equals("unmute")) {
            muteManager.unmute(target.getUniqueId());
        }
        if (action.equals("mute")) {
            if (args.length < 4) {
                return true;
            }
            try {
                long amount = Long.parseLong(args[2]);
                String unit = args[3];
                long duration = parseTime(amount, unit);

                if (duration <= 0) {
                    return true;
                }

                muteManager.mute(target.getUniqueId(), duration);

                String timeStr = amount + " " + unit;

                if (target.isOnline() && target.getPlayer() != null) {
                }
            } catch (NumberFormatException e) {
            }
        }
        return true;
    }
}
// By LazyCato0o