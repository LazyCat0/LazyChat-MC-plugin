package lazy.dev.lazyChat.commands.sign;

import lazy.dev.lazyChat.LanguageManager;
import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FormatCommand implements CommandExecutor {
    private final LanguageManager lang;
    private final LazyChat plugin;
    MiniMessage mm = MiniMessage.miniMessage();
    public FormatCommand(LazyChat plugin, LanguageManager lang) {
        this.plugin = plugin;
        this.lang = lang;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender source, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) source;
        Block targetBlock = player.getTargetBlockExact(5);

        if (!(source instanceof Player)) {
            source.sendMessage(lang.getFormated("Only for players"));
            return true;
        }

        if (!source.hasPermission("l-chat.plaque_format")) {
            source.sendMessage(lang.getFormated("LC_cannot_use"));
            return true;
        }
        if (args.length == 0) {
            source.sendMessage(lang.getFormated("SFC_usage"));
            return true;
        }
        int line = Integer.parseInt(args[0]);
        if (targetBlock == null) {
            source.sendMessage(lang.getFormated("SFC_smth_went_wrong"));
            return true;
        }
        if (targetBlock.getState() instanceof Sign sign) {
            try {
                if (line < 1 || line > 4) source.sendMessage(lang.getFormated("SFC_usage"));

                var side = sign.getSide(Side.FRONT);

                String rawText = Arrays.stream(args)
                        .skip(1)
                        .collect(Collectors.joining(" "));

                var component = MiniMessage.miniMessage().deserialize(rawText);
                side.line(line - 1, component);
                sign.update();
            } catch (Exception e) {
                source.sendMessage(lang.getFormated("SFC_smth_went_wrong"));
                plugin.getLogger().severe("While try to set new text plugin meet error: " + e);
            }
            return true;
        } else {
            source.sendMessage(lang.getFormated("SFC_smth_went_wrong"));
            return true;
        }
    }
}
