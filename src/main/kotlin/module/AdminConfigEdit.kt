package com.yulin.module

import com.yulin.AmusementPlugin.save
import com.yulin.config.AdminConfig
import com.yulin.kotlinUtil.AdminAndMasterJudge
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChainBuilder

object AdminConfigEdit {
    /**
     * 设定机器人名称，只能master来设定
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun botNameSetting(event: MessageEvent) {
        if (!AdminAndMasterJudge.isMaster(event)) {
            return
        }
        if (event.message.contentToString().startsWith("机器人名称=")) {
            try {
                AdminConfig.botName = event.message.contentToString().split("=")[1]
                AdminConfig.save()
                event.subject.sendMessage("设置机器人名称为“${AdminConfig.botName}”成功！")
            } catch (e: Exception) {
                var name = AdminConfig.botName
                if (name == "") {
                    name = event.bot.nameCardOrNick
                }
                event.subject.sendMessage("设置机器人名称失败，机器人名称为${name}")
            }

        }
    }

    /**
     * 查看机器人当前名称，只能master
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun botNameLook(event: MessageEvent) {
        if (!AdminAndMasterJudge.isMaster(event)) {
            return
        }
        if (event.message.contentToString().startsWith("机器人名称")) {
            var name = AdminConfig.botName
            if (name == "") {
                name = event.bot.nameCardOrNick
                AdminConfig.botName = name
                AdminConfig.save()
            }
            event.subject.sendMessage("机器人目前名称为“${name}”")
        }
    }

    /**
     * 设定插件管理员，只能master来设定
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun adminSetting(event: MessageEvent){
        if (AdminAndMasterJudge.isMaster(event) && event.message.contentToString().contains("设置管理员")){
            AdminConfig.adminList.add(event.message.contentToString().split("员")[1].toLong())
            AdminConfig.save()
            event.subject.sendMessage("已经成功设定管理员：${event.message.contentToString().split("员")[1]}")

        }
    }
    /**
     * 取消插件管理员，只能master来设定
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun adminSettingQc(event: MessageEvent){
        if (AdminAndMasterJudge.isMaster(event) && event.message.contentToString().contains("取消管理员")){
            AdminConfig.adminList.remove(event.message.contentToString().split("员")[1].toLong())
            AdminConfig.save()
            event.subject.sendMessage("已经成功取消管理员：${event.message.contentToString().split("员")[1]}")
        }
    }

    /**
     * 添加黑名单群，master与管理都可以添加
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun blackListSetting(event: MessageEvent): Boolean {
        if (event.message.contentToString().contains("群加黑")) {


            if (AdminAndMasterJudge.isAdminOrMaster(event)) {
                var s = event.message.contentToString().split("黑")[1]
                if (s != "") {
                    AdminConfig.blackGroupList.add(s.toLong())
                    AdminConfig.save()
                    event.subject.sendMessage("已经成功设定群黑名单：${s}")
                } else {
                    s = event.subject.id.toString()
                    AdminConfig.blackGroupList.add(s.toLong())
                    AdminConfig.save()
                    event.subject.sendMessage("已经成功设定群黑名单：${s}")
                }
            }
        }
        return false
    }

    /**
     * 去除黑名单群，master与管理都可以操作
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun blackListSettingQh(event: MessageEvent) {
        if (event.message.contentToString().contains("群去黑")) {

            if (AdminAndMasterJudge.isAdminOrMaster(event)) {
                val removeGroup = event.message.contentToString().split("黑")[1]
                val groupId: Long
                if (removeGroup != "") {
                    groupId = removeGroup.toLong()
                    AdminConfig.blackGroupList.remove(groupId)
                    AdminConfig.save()
                    event.subject
                        .sendMessage("已经成功删除群黑名单：${groupId}")
                } else {
                    groupId = event.subject.id
                    AdminConfig.blackGroupList.remove(groupId)
                    event.subject
                        .sendMessage("已成功删除群黑名单：${groupId}")
                }

            }
        }
    }



    /**
     * 列出黑名单群列表
     * @param event FriendMessageEvent好友消息事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun blackListShow(event: MessageEvent) {

        if (AdminAndMasterJudge.isAdminOrMaster(event) && event.message.contentToString().contains("黑名单群")) {
            val mess = MessageChainBuilder()
            mess.add("黑名单群列表：\n")
            for (l in AdminConfig.blackGroupList) {
                val name = event.bot.getGroup(l)?.name
                mess.add(name + "(${l})\n")
            }
            event.subject.sendMessage(mess.build())
        }
    }
}