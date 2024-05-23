package com.yulin.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupSender(
    @SerialName("gid")
    var gid: Long,
    @SerialName("sender")
    var sender: MutableList<Sender>
)
