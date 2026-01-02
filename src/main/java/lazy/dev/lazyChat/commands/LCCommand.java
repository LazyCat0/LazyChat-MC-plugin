package lazy.dev.lazyChat.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import lazy.dev.lazyChat.LazyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class LCCommand {
    private final LazyChat plugin;
    MiniMessage mm = MiniMessage.miniMessage();

    public LCCommand(LazyChat plugin) {
        this.plugin = plugin;
    }
    public void register() {
        new CommandAPICommand("lc")
                .withArguments(new StringArgument("action"))
                .withAliases("l-chat", "lazy-chat")
                .executes((sender, args) ->
                        {
                            String act = args.getRaw("action");
                            assert act != null;
                            if (act.equals(" reload") || sender.hasPermission("l-chat.reload")) {
                                try {
                                    plugin.reloadPluginConfig();
                                    sender.sendMessage(Component.text("LazyChat has successfully reloaded!", NamedTextColor.GREEN));
                                } catch (Exception e) {
                                    sender.sendMessage(Component.text("LazyChat has meet error while reloading: " + e.getLocalizedMessage(), NamedTextColor.RED));
                                    plugin.getLogger().severe("Plugin has found error while reloading: " + e.getMessage());

                                }
                            }
                            if (act.equals(" info") || sender.hasPermission("l-chat.info")) {
                                Component infoMessage = mm.deserialize("""
                                        <color:#70c4ff>Lazy-Plugin "LazyChat"</color>
                                        <color:#70c4ff>Author of plugin: LazyCato0o</color> (<click:open_url:'https://ru.namemc.com/profile/LazyCato0o.1'>NameMC</click>)
                                        <color:#70c4ff>Available on</color> <click:open_url:'https://github.com/LazyCat0/LazyChat-MC-plugin'><color:#e761ff>Github</color></click>, <click:open_url:'https://www.spigotmc.org/resources/lazychat.130059/'><color:#fff757>SpigotMC</color></click>.""");
                                sender.sendMessage(infoMessage);
                            }
                            else {
                                sender.sendMessage(mm.deserialize("<b><red>Unknown argument, correct is \"info\" and \"reload\"</red></b>"));
                            }
                        }
                );
    }
}


