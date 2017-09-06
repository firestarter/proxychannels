package com.algorithmjunkie.mc.proxychannels.channel

import com.algorithmjunkie.mc.proxychannels.ProxyChannelsPlugin

class ChannelManager(private val plugin: ProxyChannelsPlugin) {
    val channelsConfig: ChannelsConfig = ChannelsConfig(plugin)
    val channels: ArrayList<Channel> = ArrayList()

    fun purge() {
        channels.forEach { plugin.commandManager.unregisterCommand(it.command) }
        channels.clear()
    }
}