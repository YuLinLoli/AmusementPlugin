package com.mirai.config

import com.mirai.pojo.GroupImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object GroupImageConfig : AutoSavePluginConfig("GroupImageConfig") {
    @ValueName("groupImage")
    var groupImage: MutableList<GroupImage> by value()
}