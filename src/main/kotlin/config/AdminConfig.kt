package com.yulin.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object AdminConfig: AutoSavePluginConfig("AdminConfig") {
    @ValueName("master")
    var master: Long by value()
    @ValueName("admins")
    var adminList: MutableList<Long> by value()
    @ValueName("blackGroups")
    var blackGroupList: MutableList<Long> by value()
    @ValueName("cdTime")
    var cdTime: Int by value(3600)

}