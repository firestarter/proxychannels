package com.algorithmjunkie.mc.proxychannels.channel

import com.algorithmjunkie.mc.proxychannels.ProxyChannelsPlugin
import com.algorithmjunkie.mc.proxychannels.command.ChannelCommand
import com.algorithmjunkie.mc.proxychannels.command.ProxyChannelsCommand
import com.imaginarycode.minecraft.redisbungee.RedisBungee
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
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

    fun sendMessage(sender: CommandSender, message: String) {
        if (sender is ProxiedPlayer) {
            if (!members.containsKey(sender.uniqueId)) return
            val member = members[sender.uniqueId]!!

            if (member.muted) {
                sender.sendMessage(
                        ChatMessageType.ACTION_BAR,
                        *ComponentBuilder("You currently have ").color(ChatColor.YELLOW)
                                .append("$name ").color(ChatColor.LIGHT_PURPLE)
                                .append("muted.").color(ChatColor.YELLOW)
                                .create()
                )
                return
            }
        }

        val senderServerName: String = if (sender is ProxiedPlayer && ProxyChannelsPlugin.instance.isRedisBungeePresent()) {
            RedisBungee.getApi().getServerFor(sender.uniqueId).name
        } else {
            "Proxy"
        }

        val format = TextComponent(ChatColor.translateAlternateColorCodes(
                '&',
                this.format
                        .replace("%server%", senderServerName)
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