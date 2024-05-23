package com.yulin.module

import com.yulin.config.AdminConfig
import net.mamoe.mirai.event.events.MessageEvent

object Help {
    suspend fun help(event: MessageEvent) {
        if (AdminConfig.master != event.sender.id || !AdminConfig.adminList.contains(event.sender.id)) {
            return
        }
        if (event.message.contentToString().startsWith("管理员帮助")) {
            event.subject.sendMessage(
                "AmusementPlugin help\n" +
                        "设置|取消管理员123456(设置插件管理员)\n" +
                        "指令-开启|关闭(开启或者关闭插件功能，私聊触发在开启|关闭后面附加群号即可)\n" +
                        "指令-关闭的群(仅限主人私聊触发，查看关闭的群列表)\n" +
                        "指令-cd=123456(设置草群友的cd，单位秒)"
            )
        }
    }
}