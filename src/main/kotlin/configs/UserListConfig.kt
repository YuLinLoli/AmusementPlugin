package com.mirai.configs

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object UserListConfig : AutoSavePluginConfig("UserListConfig") {

    @ValueName("groupBlacks")
    var groupBlackList: MutableList<Long> by value()

    @ValueName("peopleBlacks")
    var peopleBlackList: MutableList<Long> by value()

    @ValueName("admins")
    var adminList: MutableList<Long> by value()
}