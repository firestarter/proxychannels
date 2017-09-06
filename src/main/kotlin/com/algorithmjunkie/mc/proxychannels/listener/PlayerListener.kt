package com.algorithmjunkie.mc.proxychannels.listener

import com.algorithmjunkie.mc.proxychannels.ProxyChannelsPlugin
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PlayerListener(private val plugin: ProxyChannelsPlugin) : Listener {
    @EventHandler
    fun onJoin(event: PostLoginEvent) {
        plugin.channelManager.channels.forEach {
            it.addMember(event.player)
        }
    }

    @EventHandler
    fun onLeave(event: PlayerDisconnectEvent) {
        val player = event.player
        plugin.channelManager.channels.forEach {
            if (it.hasMember(player)) {
                it.removeMember(player)
            }
        }
    }
}