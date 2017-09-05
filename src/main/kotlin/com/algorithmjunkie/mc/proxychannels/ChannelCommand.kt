package com.algorithmjunkie.mc.proxychannels

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.Default
import net.md_5.bungee.api.CommandSender

class ChannelCommand(private val channel: Channel): BaseCommand(channel.name) {
    @Default
    fun onExecute(sender: CommandSender, args: Array<String>) {
        channel.sendMessage(sender, if (args.isEmpty()) "hello" else args.joinToString(" "))
    }
}