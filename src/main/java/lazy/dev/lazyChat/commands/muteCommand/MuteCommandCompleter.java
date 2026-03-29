package lazy.dev.lazyChat.commands.muteCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MuteCommandCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return List.of("mute", "unmute");
        }
        if (args.length == 2) {
            List<String> playerNames = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                playerNames.add(p.getName());
            }
            List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[2], playerNames, completions);
            Collections.sort(completions);
            return completions;
        }
        if (args.length == 4) {
            return List.of("minute", "hour", "day");
        }
        return List.of();
    }
}
// by LazyCato0o