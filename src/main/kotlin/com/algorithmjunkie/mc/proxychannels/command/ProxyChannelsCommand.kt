package com.algorithmjunkie.mc.proxychannels.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import com.algorithmjunkie.mc.proxychannels.ProxyChannelsPlugin
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder

@CommandAlias("proxychannels|pc")
class ProxyChannelsCommand(private val plugin: ProxyChannelsPlugin) : BaseCommand() {
    @Subcommand("reload|rel")
    @CommandPermission("proxychannels.admin")
    @Default
    fun onReload(sender: CommandSender) {
        plugin.channelManager.channelsConfig.reload()
        sender.sendMessage(
                *ComponentBuilder("You reloaded the ProxyChannels configuration.").color(ChatColor.GREEN)
                        .create()
        )
    }
}