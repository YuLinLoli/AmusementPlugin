package com.yulin.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("groupImage")
data class GroupImage(var groupId: Long, var tf: Boolean, var imageList: MutableList<String>)
