package com.algorithmjunkie.mc.proxychannels.listener

import com.algorithmjunkie.mc.proxychannels.ProxyChannelsPlugin
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class ChatListener(private val plugin: ProxyChannelsPlugin) : Listener {
    @EventHandler
    fun onChat(event: ChatEvent) {
        if (event.isCommand) return
        if (event.sender !is ProxiedPlayer) return
        if (event.message.length <= 1) return

        val first = event.message[0]

        plugin.channelManager.channels.forEach {
            println(it.name)
            if (it.triggers.contains(first)) {
                event.isCancelled = true
                val message = event.message.substring(1).trim()
                it.sendMessage(event.sender as ProxiedPlayer, message)
                return@forEach
            }
        }
    }
}