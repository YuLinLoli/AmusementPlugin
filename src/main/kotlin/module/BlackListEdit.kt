package com.yulin.module

import com.yulin.config.BlackListConfig.blackList
import com.yulin.kotlinUtil.AdminAndMasterJudge
import com.yulin.pojo.BlackPojo
import net.mamoe.mirai.event.events.MessageEvent

object BlackListEdit {
    suspend fun add(event: MessageEvent) {
        val contentToString = event.message.contentToString()
        if (contentToString.startsWith("加黑") && AdminAndMasterJudge.isMaster(event)) {
            val split = contentToString.split("黑")[1].split("-")
            blackList.add(BlackPojo(split[0].toLong(), split[1]))
            event.subject.sendMessage("添加黑名单成功！")
        }
    }

    suspend fun del(event: MessageEvent): Boolean {
        if (blackList.size != 0) {
            event.subject.sendMessage("黑名单列表为空无法删除！")
            return false
        }
        val l = event.message.contentToString().split("黑")[1].toLong()
        for (b in blackList) {
            if (b.qq == l) {
                blackList.remove(b)
                event.subject.sendMessage("删除" + l + "黑名单成功！")
                return true
            }
        }
        event.subject.sendMessage("删除失败，请检查qq是否正确！")
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
            event.subject.sendMessage(str)
        } else {
            event.subject.sendMessage("黑名单列表为空！")
        }
    }

}