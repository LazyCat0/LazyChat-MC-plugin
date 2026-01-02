package lazy.dev.lazyChat.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BCCommand {
    private final LazyChat plugin;
    MiniMessage mm = MiniMessage.miniMessage();

    public BCCommand(LazyChat plugin) {
        this.plugin = plugin;
    }
    public void register() {
        new CommandAPICommand("broadcast")
                .withArguments(new StringArgument("message"))
                .withAliases("bc")
                .withPermission("l-chat.broadcast")
                .executes((sender, args) ->
                        {
                            String message = args.getRaw("message");
                            Component formatedMessage = mm.deserialize("<b><gold>New broadcast!</gold></b>\n\n" + message + "Broadcast by<gold>" + sender.getName() + "</gold>");
                            if (message == null || sender.hasPermission("l-chat.broadcast")) {
                                sender.sendMessage(mm.deserialize("<red>You cannot send <b>empty</b> message!</red>"));
                            }
                            if (sender.hasPermission("l-chat.broadcast")) {
                                Player Players = (Player) Bukkit.getOnlinePlayers();
                                Players.sendMessage(formatedMessage);
                            }
                        }
                );
    }
}
