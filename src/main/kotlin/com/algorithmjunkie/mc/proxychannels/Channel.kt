package com.algorithmjunkie.mc.proxychannels

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import kotlin.collections.ArrayList

class Channel(val name: String,
              val triggers: List<Char>,
              val permission: String,
              private val format: String
) {
    val members: ArrayList<CommandSender> = ArrayList()
    val command: ChannelCommand = ChannelCommand(this)

    fun sendMessage(sender: CommandSender, message: String) {
        if (sender is ProxiedPlayer && !members.contains(sender)) {
            println("sender unknown")
            return
        }

        val format = TextComponent(ChatColor.translateAlternateColorCodes(
                '&',
                this.format
                        .replace("%server%", if (sender is ProxiedPlayer) sender.server.info.name else "Proxy")
                        .replace("%user%", if (sender is ProxiedPlayer) sender.displayName else sender.name)
                        .replace("%message%", message)
        ))

        members.forEach {
            it.sendMessage(format)
        }

        println(format.toLegacyText())
    }

    fun addMember(member: CommandSender): Boolean {
        if (permission.isBlank() || member.hasPermission(permission)) {
            members.add(member)
            return true
        }
        return false
    }

    fun hasMember(member: CommandSender): Boolean {
        return members.contains(member)
    }

    fun removeMember(member: CommandSender) {
        members.remove(member)
    }
}