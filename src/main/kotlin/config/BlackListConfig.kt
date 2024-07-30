package com.yulin.config

import com.yulin.pojo.BlackSender
import com.yulin.pojo.Sender
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object BlackListConfig : AutoSavePluginConfig("BlackListConfig") {
    @ValueName("blackList")
    var blackList: MutableList<BlackSender> by value()

    @ValueName("memberDontWantToCao")
    var memberDontWantToCao: MutableList<Sender> by value()
}