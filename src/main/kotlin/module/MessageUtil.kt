package com.yulin.module

import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.findIsInstance
import net.mamoe.mirai.message.data.recallSource
import net.mamoe.mirai.message.data.source

object MessageUtil {
    //撤回被引用的消息
    suspend fun recallMessage(event: GroupMessageEvent): Boolean{
        if (event.message.contentToString().contains("撤") && event.message.contains(QuoteReply)) {
            val findIsInstance = event.message.findIsInstance<QuoteReply>()
            //如果是管理员就直接撤回
            if (event.group.botAsMember.permission.isOperator()){
                findIsInstance?.recallSource()
                return true
            }
            //如果是自己发送的消息，且时间小于116秒就撤回
            if (findIsInstance?.source!!.fromId == event.bot.id && event.message.source.time - findIsInstance.source.time < 120){
                findIsInstance.recallSource()
                println("已经过去了${event.message.source.time - findIsInstance.source.time}秒，可以撤回！撤回成功！")
                return true
            }
            if (findIsInstance.source.fromId == event.bot.id){
                event.subject.sendMessage("撤回失败！时间超过两分钟！")
                return false
            }
        }
        return false
    }
}