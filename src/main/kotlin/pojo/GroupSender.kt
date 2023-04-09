package com.mirai.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("groupSender")
data class GroupSender(var gid: Long, var sender: MutableList<Sender>)