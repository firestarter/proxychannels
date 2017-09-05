package com.algorithmjunkie.mc.proxychannels

class ChannelManager(private val plugin: ProxyChannelsPlugin) {
    val channelsConfig: ChannelsConfig = ChannelsConfig(plugin)
    val channels: ArrayList<Channel> = ArrayList()

    fun purge() {
        channels.forEach { plugin.commandManager.unregisterCommand(it.command) }
        channels.clear()
    }
}