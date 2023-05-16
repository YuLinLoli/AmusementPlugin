package com.mirai.module

import com.mirai.config.AdminConfig
import com.mirai.config.BlackListConfig
import com.mirai.config.BlackListConfig.blackList
import com.mirai.pojo.BlackPojo
import net.mamoe.mirai.event.events.MessageEvent

object BlackListEdit {
    suspend fun add(event: MessageEvent) {
        val contentToString = event.message.contentToString()
        if (contentToString.startsWith("加黑") && AdminConfig.master == event.sender.id) {
            val split = contentToString.split("黑")[1].split("-")
            blackList.add(BlackPojo(split[0].toLong(), split[1]))
        }
    }

    suspend fun del(event: MessageEvent): Boolean {
        if (blackList.size != 0) {
            event.sender.sendMessage("黑名单列表为空无法删除！")
            return false
        }
        val l = event.message.contentToString().split("黑")[1].toLong()
        for (b in blackList) {
            if (b.qq == l) {
                blackList.remove(b)
                event.sender.sendMessage("删除" + l + "黑名单成功！")
                return true
            }
        }
        event.sender.sendMessage("删除失败，请检查qq是否正确！")
        return false
    }

    suspend fun show(event: MessageEvent) {
        if (blackList.size != 0) {
            var int = 0
            val str = "名单列表\r"
            for (l in blackList) {
                int++
                str.plus("" + int + ":" + l.qq + "拉黑原因：" + l.text + "\r")
            }
            event.sender.sendMessage(str)
        } else {
            event.sender.sendMessage("黑名单列表为空！")
        }
    }

}