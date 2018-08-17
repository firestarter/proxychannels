package com.algorithmjunkie.mc.proxychannels.listener

import com.algorithmjunkie.mc.proxychannels.ProxyChannelsPlugin
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

class RedisBungeeListener(private val plugin: ProxyChannelsPlugin): Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onMessageReceived(event: PubSubMessageEvent) {
        if (event.channel != plugin.redisBungeeChannelName) return
        val split = event.message.split(":::")
        val channel = plugin.channelManager.getChannelByName(split[0])
        if (channel != null) {
            channel.members.filter { !it.value.muted }
                    .map { it.value.member }
                    .forEach { it.sendMessage(*TextComponent.fromLegacyText(split[1])) }

            println(ChatColor.stripColor(split[1]))
        }
    }
}