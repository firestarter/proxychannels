package com.algorithmjunkie.mc.proxychannels.channel

import net.md_5.bungee.api.connection.ProxiedPlayer

data class ChannelMember(val member: ProxiedPlayer) {
    var toggled: Boolean = false
    var muted: Boolean = false
}