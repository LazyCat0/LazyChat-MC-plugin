package dev.lazycat.lazyChat.commands

import dev.lazycat.lazyChat.LazyChat
import dev.lazycat.lazyChat.api.language.LanguageManager
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class LCCommandKt(private val plugin: LazyChat) : CommandExecutor, TabCompleter {

    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            return true
        }

        val act: String = args[0].lowercase()
        when (act){
            "reload" -> {
                if (!sender.hasPermission("l-chat.reload")) {
                    return true
                }
                try {
                    plugin.reloadConfig()
                    plugin.logger.info("Successfully reloaded plugin")
                } catch (e: Exception ) {
                    plugin.logger.severe("While plugin reloads, plugin was meets error - " + e.message)
                }
            }
            "info" -> sender.sendMessage("wip")
            else -> {
                sender.sendMessage("wip")
            }
        }


        return true
    }

    override fun onTabComplete(
        s: CommandSender,
        c: Command,
        l: String,
        a: Array<out String>
    ): List<String?> {
        if (a.size == 1) {
            return mutableListOf<String?>("reload", "info")
        }
        return mutableListOf<String?>()
    }
}