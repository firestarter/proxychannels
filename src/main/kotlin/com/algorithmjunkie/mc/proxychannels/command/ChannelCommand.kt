package com.algorithmjunkie.mc.proxychannels.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.UnknownHandler
import com.algorithmjunkie.mc.proxychannels.channel.Channel
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.connection.ProxiedPlayer

class ChannelCommand(private val channel: Channel) : BaseCommand(channel.name) {
    @Default
    @UnknownHandler
    fun onExecute(sender: CommandSender, args: Array<String>) {
        if (sender == ProxyServer.getInstance().console || sender is ProxiedPlayer && channel.hasMember(sender)) {
            if (args.isEmpty()) {
                if (sender is ProxiedPlayer) {
                    val member = channel.getMember(sender)!!
                    member.toggled = !member.toggled
                    sender.sendMessage(
                            ChatMessageType.ACTION_BAR,
                            *ComponentBuilder("You toggled ").color(ChatColor.YELLOW)
                                    .append(if (member.toggled) "into" else "out of")
                                    .append(" ${channel.name}").color(ChatColor.LIGHT_PURPLE)
                                    .append(".").color(ChatColor.YELLOW)
                                    .create()
                    )
                } else {
                    sender.sendMessage(
                            *ComponentBuilder("Console must provide a message to send.").color(ChatColor.RED)
                                    .create()
                    )
                }
                return
            }
            channel.sendMessage(sender, args.joinToString(" "))
        }
    }

    @Subcommand("mute")
    fun onToggleMute(sender: ProxiedPlayer) {
        if (channel.hasMember(sender)) {
            val member = channel.getMember(sender)!!
            member.muted = !member.muted
            sender.sendMessage(
                    ChatMessageType.ACTION_BAR,
                    *ComponentBuilder("You ").color(ChatColor.YELLOW)
                            .append((if (!member.muted) "un" else "") + "muted")
                            .append(" ${channel.name}").color(ChatColor.LIGHT_PURPLE)
                            .append(".").color(ChatColor.YELLOW)
                            .create()
            )
        }
    }
}