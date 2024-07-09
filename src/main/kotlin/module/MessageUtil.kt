package com.yulin.module

import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*

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
    suspend fun currentTimestampToDay(millis: Long): String {
        val totalSeconds = millis / 1000
        val totalMinutes = totalSeconds / 60
        val totalHours = totalMinutes / 60
        val totalDays = totalHours / 24

        val hours = totalHours % 24
        val minutes = totalMinutes % 60
        return "${totalDays}天${hours}时${minutes}分"
    }
    /**
     * 找到消息链中的第一个@
     * @param event 群消息事件
     * @return 空或者@对象
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    fun findAt(event: GroupMessageEvent): At?{
        return event.message.find {
            it is At
        }.let {
            if (it != null){
                it as At
            }else{
                null
            }
        }
    }
}