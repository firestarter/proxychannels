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

        val player = event.sender as ProxiedPlayer

        plugin.channelManager.channels.forEach {
            if (it.hasMember(player) && it.getMember(player)!!.toggled) {
                event.isCancelled = true
                it.sendMessage(player, event.message)
                return@forEach
            } else if (event.message.length > 1 && it.triggers.contains(event.message[0])) {
                event.isCancelled = true
                val message = event.message.substring(1).trim()
                it.sendMessage(player, message)
                return@forEach
            }
        }
    }
}