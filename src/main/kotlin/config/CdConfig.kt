package com.yulin.config

import com.yulin.pojo.GroupSender
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object CdConfig : AutoSavePluginConfig("CdConfig") {
    @ValueName("groupSender")
    var groupSender: MutableList<GroupSender> by value()
}