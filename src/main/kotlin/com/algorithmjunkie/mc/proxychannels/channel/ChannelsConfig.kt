package com.algorithmjunkie.mc.proxychannels.channel

import com.algorithmjunkie.mc.proxychannels.ProxyChannelsPlugin
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
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
        loadChannels().forEach {
            plugin.channelManager.channels.add(it)
        }
    }

    private fun loadChannels(): List<Channel> {
        val oldChannels: HashMap<String, Channel> = plugin.channelManager.channels.associateBy { it.name } as HashMap<String, Channel>
        plugin.channelManager.purge()
        val newChannels: HashMap<String, Channel> = HashMap()
        data.keys.forEach { channelName ->
            val sect = data.getSection(channelName)

            val channel = Channel(
                    channelName,
                    sect.getStringList("triggers").map { it[0] },
                    sect.getString("permission") ?: "",
                    sect.getString("format")
            )

            val addMessage = ComponentBuilder("You are now a member of ").color(ChatColor.YELLOW)
                    .append(channelName).color(ChatColor.LIGHT_PURPLE)
                    .append(".").color(ChatColor.YELLOW)
                    .create()

            plugin.proxy.players.forEach {
                if (channel.addMember(it)) {
                    it.sendMessage(*addMessage)
                }
            }

            plugin.commandManager.registerCommand(channel.command)
            newChannels.put(channelName, channel)
        }

        newChannels.forEach {
            oldChannels.remove(it.key)
            oldChannels.map { it.value }.forEach {
                val info = ComponentBuilder("${it.name} ").color(ChatColor.LIGHT_PURPLE)
                        .append("has been renamed or removed.").color(ChatColor.YELLOW)
                        .create()
                it.members.forEach { member -> member.value.member.sendMessage(*info) }
            }
        }

        return newChannels.map { it.value }
    }
}