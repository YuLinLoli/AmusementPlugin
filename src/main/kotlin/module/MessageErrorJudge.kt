package com.yulin.module

import com.yulin.AmusementPlugin.logger
import com.yulin.config.AdminConfig
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupMessagePostSendEvent
import net.mamoe.mirai.event.events.MessagePostSendEvent

object MessageErrorJudge {

    suspend fun <C : Contact> messageJudge(event: MessagePostSendEvent<C>){
        var name = "私聊"
        var name1 = "QQ"
        var bol = false
        if (event is GroupMessagePostSendEvent){
            name = "群"
            name1 = name
            bol = true
        }
        val master = event.bot.getFriend(AdminConfig.master)
        val ids = event.receipt?.source?.ids ?: IntArray(0)
        if ((ids.isEmpty() || ids.any { it < 0 }) && master != null && bol) {
            master.sendMessage("${name}消息发送失败，你的账号可能已被腾讯风控！${name1}号：${event.receipt?.target?.id}")
        }else{
            logger.warning("${name}消息发送失败，你的账号可能已被腾讯风控！${name1}号：${event.receipt?.target?.id}")
        }
    }
}