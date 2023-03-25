package com.mirai.module

import com.mirai.AmusementPlugin.save
import com.mirai.config.AdminConfig
import net.mamoe.mirai.event.events.MessageEvent

object AdminConfigEdit {
    /**
     * 设定插件管理员，只能master来设定
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun adminSetting(event: MessageEvent){
        if (AdminConfig.master == event.sender.id && event.message.contentToString().contains("设置管理员")){
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
        if (AdminConfig.master == event.sender.id && event.message.contentToString().contains("取消管理员")){
            AdminConfig.adminList.remove(event.message.contentToString().split("员")[1].toLong())
            AdminConfig.save()
            event.sender.sendMessage("已经成功取消管理员：${event.message.contentToString().split("员")[1]}")
        }
    }

    /**
     * 添加黑名单群，master与管理都可以添加
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun blackListSetting(event: MessageEvent){
        if (event.message.contentToString().contains("群加黑")){
            var b = false
            if (AdminConfig.master == event.sender.id){
                b = true
            }
            for (l in AdminConfig.adminList) {
                if (l == event.sender.id){
                    b = true
                }
            }
            if (b){
                AdminConfig.blackGroupList.add(event.message.contentToString().split("黑")[1].toLong())
                AdminConfig.save()
                event.sender.sendMessage("已经成功设定群黑名单：${event.message.contentToString().split("黑")[1]}")
            }
        }
    }
    /**
     * 去除黑名单群，master与管理都可以操作
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun blackListSettingQh(event: MessageEvent){
        if (event.message.contentToString().contains("群去黑")){
            var b = false
            if (AdminConfig.master == event.sender.id){
                b = true
            }
            for (l in AdminConfig.adminList) {
                if (l == event.sender.id){
                    b = true
                }
            }
            if (b){
                AdminConfig.blackGroupList.remove(event.message.contentToString().split("黑")[1].toLong())
                AdminConfig.save()
                event.sender.sendMessage("已经成功删除群黑名单：${event.message.contentToString().split("黑")[1]}")
            }
        }
    }
}