package com.algorithmjunkie.mc.proxychannels.listener

import com.algorithmjunkie.mc.proxychannels.ProxyChannelsPlugin
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class ChatListener(private val plugin: ProxyChannelsPlugin): Listener {
    @EventHandler
    fun onChat(event: ChatEvent) {
        if (event.isCommand) return
        if (event.sender !is CommandSender) return

        plugin.channelManager.channels.find { it.triggers.contains(event.message.toLowerCase()[0]) }?.sendMessage(
                event.sender as CommandSender,
                event.message
        )
    }
}