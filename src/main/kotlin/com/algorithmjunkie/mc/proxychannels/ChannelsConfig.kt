package com.algorithmjunkie.mc.proxychannels

import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.nio.file.Files

class ChannelsConfig(private val plugin: ProxyChannelsPlugin) {
    private val file: File = File(plugin.dataFolder, "channels.yml")
    lateinit var data: Configuration

    init {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdir()
        if (!file.exists()) Files.copy(plugin.getResourceAsStream("channels.yml"), file.toPath())
    }

    fun reload() {
        data = YamlConfiguration.getProvider(YamlConfiguration::class.java).load(file)
        plugin.channelManager.purge()
        loadChannels().forEach {
            plugin.channelManager.channels.add(it)
        }
    }

    private fun loadChannels(): List<Channel> {
        val oldChannels: ArrayList<Channel> = plugin.channelManager.channels
        val newChannels: ArrayList<Channel> = ArrayList()
        data.keys.forEach { channelName ->
            val sect = data.getSection(channelName)

            val channel = Channel(
                    channelName,
                    sect.getCharList("triggers") ?: ArrayList(),
                    sect.getString("permission") ?: "",
                    sect.getString("format")
            )

            newChannels.add(channel)

            oldChannels.find { old -> old.name.equals(channel.name, true) }?.members?.forEach { member ->
                if (member is ProxiedPlayer && plugin.proxy.getPlayer(member.uniqueId) != null) {
                    channel.addMember(member)
                }
            }

            plugin.commandManager.registerCommand(channel.command)
        }
        return newChannels
    }
}