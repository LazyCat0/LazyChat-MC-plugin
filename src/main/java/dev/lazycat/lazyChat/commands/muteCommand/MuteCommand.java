package dev.lazycat.lazyChat.commands.muteCommand;

import dev.lazycat.lazyChat.LazyChat;
import dev.lazycat.lazyChat.api.language.LanguageManager;
import dev.lazycat.lazyChat.api.mute.MuteManager;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

    public MuteCommand(LazyChat plugin) {
        this.muteManager = plugin.muteManager;
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) {
            sender.sendMessage(lang.getFormated("mute.command.usage"));
            return true;
        }
        String action = args[0].toLowerCase();
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (action.equals("unmute")) {
            muteManager.unmute(target.getUniqueId());
            sender.sendMessage(lang.getFormated("mute.messages.unmuted",
                    Placeholder.parsed("player", Objects.requireNonNull(target.getName()))
            ));
            if (target.isOnline()) {
                Objects.requireNonNull(target.getPlayer()).sendMessage(lang.getFormated("mute.messages.yj_unmuted"));
            }
        }
        if (action.equals("mute")) {
            if (args.length < 4) {
                sender.sendMessage(lang.getFormated("mute.command.set_time"));
                return true;
            }
            try {
                long amount = Long.parseLong(args[2]);
                String unit = args[3];
                long duration = parseTime(amount, unit);

                if (duration <= 0) {
                    sender.sendMessage(lang.getFormated("mute.command.set_time"));
                    return true;
                }

                muteManager.mute(target.getUniqueId(), duration);

                String timeStr = amount + " " + unit;

                if (target.isOnline() && target.getPlayer() != null) {
                    Objects.requireNonNull(target.getPlayer()).sendMessage(lang.getFormated("mute.messages.yj_muted", Placeholder.parsed("time", timeStr)));
                    sender.sendMessage(lang.getFormated("mute.messages.muted",
                            Placeholder.parsed("player", target.getName()),
                            Placeholder.parsed("time", timeStr)
                            ));
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(e.getMessage());
            }
        }
        return true;
    }
}
// By LazyCato0o