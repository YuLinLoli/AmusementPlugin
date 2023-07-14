package com.yulin.config

import com.yulin.pojo.GroupImage
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object GroupImageConfig : AutoSavePluginConfig("GroupImageConfig") {
    @ValueName("groupImage")
    var groupImage: MutableList<GroupImage> by value()
    @ValueName("groupImageAd")
    var groupImageAd: String by value("添加表情")
}