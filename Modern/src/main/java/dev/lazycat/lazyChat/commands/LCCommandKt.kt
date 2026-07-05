package dev.lazycat.lazyChat.commands

import dev.lazycat.lazyChat.LazyChat
import dev.lazycat.lazyChat.api.language.LangHelper
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class LCCommandKt(private val plugin: LazyChat, private val lang: LangHelper) : CommandExecutor, TabCompleter {

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
                    val message: Component = lang.translate(sender as Player, "lazychat.messages.cannot_use")
                    sender.sendMessage(message)
                    return true
                }
                val m0: Component = lang.translate(sender as Player, "lazychat.reload.success")
                sender.sendMessage(m0)
                try {
                    plugin.reloadConfig()
                    val message: Component = lang.translate(sender, "lazychat.reload.success")
                    sender.sendMessage(message)
                    plugin.logger.info("Successfully reloaded plugin")
                } catch (e: Exception ) {
                    val placeholder: Map<String, Component> = mapOf(
                        "e_message" to Component.text(e.message)
                    )
                    val message: Component = lang.translateWithPlaceholders(sender, "lazychat.reload.error", placeholder)
                    plugin.logger.severe("While plugin reloads, plugin was meets error - " + e.message)
                }
            }
            "info" -> sender.sendMessage(lang.translate(sender as Player, "lazychat.messages.info"))
            else -> {
                sender.sendMessage(lang.translate(sender as Player, "lazychat.messages.usage"))
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
        return mutableListOf()
    }
}