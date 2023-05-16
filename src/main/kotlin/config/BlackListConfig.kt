package com.mirai.config

import com.mirai.pojo.BlackPojo
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object BlackListConfig : AutoSavePluginConfig("BlackListConfig") {
    @ValueName("blackList")
    var blackList: MutableList<BlackPojo> by value()
}