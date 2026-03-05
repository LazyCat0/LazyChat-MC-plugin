package lazy.dev.lazyChat.commands.muteCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MuteCommandCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return List.of("mute", "unmute");
        }
        if (args.length == 3) {
            return List.of("1m", "1h", "1d");
        }
        return List.of();
    }
}
// by LazyCato0o