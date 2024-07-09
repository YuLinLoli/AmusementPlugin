package com.yulin.module

import com.yulin.config.BlackListConfig
import com.yulin.kotlinUtil.AdminAndMasterJudge.isAdminOrMaster
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.findIsInstance

object BlackListEdit {
    suspend fun blackListMain(e: MessageEvent){
        if (!isAdminOrMaster(e)){
            return
        }
        add(e)
        del(e)
        show(e)
    }
    private suspend fun add(event: MessageEvent) {
        val contentToString = event.message.contentToString()
        if (contentToString.startsWith("加黑")) {
            //把加黑的qq分割出来“好友加黑123456-人品不行”
            val split = contentToString.split("黑")[1].split("-")
            BlackListConfig.blackList.add(split[0].toLong())
            BlackListConfig.blackCause.add(split[1])
            event.subject.sendMessage("添加${split[0].toLong()}黑名单成功！")
        }
    }

    private suspend fun del(event: MessageEvent): Boolean {
        val contentToString = event.message.contentToString()
        if (!contentToString.startsWith("去黑")){
            return false
        }
        val at = event.message.findIsInstance<At>()
        if (at != null) {
            if (BlackListConfig.blackList.remove(at.target)) {
                event.subject.sendMessage("删除" + at.target + "黑名单成功！")
                return true
            }
        }
        if (BlackListConfig.blackList.size == 0) {
            event.subject.sendMessage("黑名单列表为空无法删除！")
            return false
        }
        val l = event.message.contentToString().split("黑")[1].toLong()
        if (BlackListConfig.blackList.remove(l)) {
                event.subject.sendMessage("删除" + l + "黑名单成功！")
                return true
            }

        event.subject.sendMessage("删除失败，请检查qq是否正确！")
        return false
    }

    private suspend fun show(event: MessageEvent) {
        if (event.message.contentToString() != "黑名单列表"){
            return
        }
        if (BlackListConfig.blackList.size != 0) {
            var int = 0
            var str = "名单列表\r"
            for (l in BlackListConfig.blackList) {
                int++
                str += "" + int + ":" + l + "，拉黑原因：" + BlackListConfig.blackCause[int - 1] + "\r"
            }
            event.subject.sendMessage(str)
        } else {
            event.subject.sendMessage("黑名单列表为空！")
        }
    }

}