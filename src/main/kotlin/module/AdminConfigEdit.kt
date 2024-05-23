package com.yulin.module

import com.yulin.AmusementPlugin.save
import com.yulin.config.AdminConfig
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChainBuilder

object AdminConfigEdit {
    /**
     * 设定插件管理员，只能master来设定
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun adminSetting(event: MessageEvent){
        if (AdminConfig.master == event.sender.id && event.message.contentToString().startsWith("指令-设置管理员")) {
            AdminConfig.adminList.add(event.message.contentToString().split("员")[1].toLong())
            AdminConfig.save()
            event.sender.sendMessage("已经成功设定管理员：${event.message.contentToString().split("员")[1]}")
        }
    }
    /**
     * 取消插件管理员，只能master来设定
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun adminSettingQc(event: MessageEvent){
        if (AdminConfig.master == event.sender.id && event.message.contentToString().startsWith("指令-取消管理员")) {
            AdminConfig.adminList.remove(event.message.contentToString().split("员")[1].toLong())
            AdminConfig.save()
            event.sender.sendMessage("已经成功取消管理员：${event.message.contentToString().split("员")[1]}")
        }
    }

    /**
     * 设置cd
     */
    fun cdEdit(event: MessageEvent) {
        if (!event.message.contentToString().startsWith("指令-cd")) {
            return
        }
        AdminConfig.cdTime = event.message.contentToString().split("cd=")[1].toInt()
        AdminConfig.save()
    }

    /**
     * 添加黑名单群，master与管理都可以添加
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun blackListSetting(event: MessageEvent) {
        if (event.message.contentToString().startsWith("指令-关闭")) {
            if (AdminConfig.master != event.sender.id || !AdminConfig.adminList.contains(event.sender.id)) {
                return
            }

            var s = event.message.contentToString().split("关闭")[1]
                if (s != "") {
                    AdminConfig.blackGroupList.add(s.toLong())
                    event.subject.sendMessage("已经成功关闭群：${s}的草群友功能")
                } else {
                    s = event.subject.id.toString()
                    AdminConfig.blackGroupList.add(s.toLong())
                    event.subject.sendMessage("已经成功关闭群：${s}的草群友功能")
                }

        }
    }



    /**
     * 去除黑名单群，master与管理都可以操作
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun blackListSettingQh(event: MessageEvent) {
        if (event.message.contentToString().startsWith("指令-开启")) {
            if (AdminConfig.master != event.sender.id || !AdminConfig.adminList.contains(event.sender.id)) {
                return
            }


            val removeGroup = event.message.contentToString().split("开启")[1]
                val groupId: Long
                if (removeGroup != "") {
                    groupId = removeGroup.toLong()
                    AdminConfig.blackGroupList.remove(groupId)
                    AdminConfig.save()
                    event.subject
                        .sendMessage("已经成功开启群：${groupId}的草群友功能")
                } else {
                    groupId = event.subject.id
                    AdminConfig.blackGroupList.remove(groupId)
                    event.subject
                        .sendMessage("已经成功开启群：${groupId}的草群友功能")
                }


        }
    }



    /**
     * 列出关闭群列表
     * @param event FriendMessageEvent好友消息事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun blackListShow(event: FriendMessageEvent) {

        if (AdminConfig.master == event.sender.id && event.message.contentToString().startsWith("指令-关闭的群")) {
            val mess = MessageChainBuilder()
            mess.add("关闭群列表：\n")
            for (l in AdminConfig.blackGroupList) {
                val name = event.bot.getGroup(l)?.name
                mess.add(name + "(${l})\n")
            }
            event.sender.sendMessage(mess.build())
        }
    }
}