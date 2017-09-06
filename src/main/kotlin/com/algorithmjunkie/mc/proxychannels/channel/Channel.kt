package com.algorithmjunkie.mc.proxychannels.channel

import com.algorithmjunkie.mc.proxychannels.command.ChannelCommand
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*
import kotlin.collections.HashMap

class Channel(val name: String,
              val triggers: List<Char>,
              private val permission: String,
              private val format: String
) {
    internal val members: HashMap<UUID, ChannelMember> = HashMap()
    internal val command: ChannelCommand = ChannelCommand(this)

    init {
        val addMessage = ComponentBuilder("You are now a member of ").color(ChatColor.YELLOW)
                .append(name).color(ChatColor.LIGHT_PURPLE)
                .append(".").color(ChatColor.YELLOW)
                .create()

        ProxyServer.getInstance().players.forEach {
            addMember(it)
            it.sendMessage(*addMessage)
        }
    }

    fun sendMessage(sender: CommandSender, message: String) {
        if (sender is ProxiedPlayer) {
            if (!members.containsKey(sender.uniqueId)) return
            val member = members[sender.uniqueId]!!

            if (member.muted) {
                sender.sendMessage(
                        *ComponentBuilder(name).color(ChatColor.GREEN)
                                .append(" is currently muted.").color(ChatColor.AQUA)
                                .create()
                )
                return
            }
        }

        val format = TextComponent(ChatColor.translateAlternateColorCodes(
                '&',
                this.format
                        .replace("%server%", (sender as? ProxiedPlayer)?.server?.info?.name?.capitalize() ?: "Proxy")
                        .replace("%user%", if (sender is ProxiedPlayer) sender.displayName else "Console")
                        .replace("%message%", message)
        ))

        members.filter { !it.value.muted }
                .map { it.value.member }
                .forEach { it.sendMessage(format) }

        println(format.toLegacyText())
    }

    fun addMember(member: ProxiedPlayer): Boolean {
        if (permission.isBlank() || member.hasPermission(permission)) {
            members.put(member.uniqueId, ChannelMember(member))
            return true
        }
        return false
    }

    fun removeMember(member: ProxiedPlayer) {
        members.remove(member.uniqueId)
    }

    fun hasMember(member: ProxiedPlayer): Boolean {
        return members.containsKey(member.uniqueId)
    }

    fun getMember(member: ProxiedPlayer): ChannelMember? {
        return members[member.uniqueId]
    }
}