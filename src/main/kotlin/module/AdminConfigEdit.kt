package com.yulin.module

import com.yulin.AmusementPlugin.logger
import com.yulin.AmusementPlugin.save
import com.yulin.config.AdminConfig
import com.yulin.kotlinUtil.AdminAndMasterJudge
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChainBuilder

object AdminConfigEdit {
    /**
     * 本函数用于设置机器人的名称，但仅限于机器人的主人执行。
     * 收到的消息需要遵循特定格式："机器人名称=新名称"。
     * 如果格式正确，将更新机器人的名称并保存配置；若格式错误或名称为空，
     * 将向发送者反馈相应的错误信息。
     *
     * @param event 消息事件对象，可以是群聊或私聊，用于识别发送者和解析消息内容。
     */
    suspend fun botNameSetting(event: MessageEvent) {
        // 验证事件的发送者是否为机器人的主人，如果不是，则直接退出函数。
        if (!AdminAndMasterJudge.isMaster(event)) {
            return
        }

        // 检查消息内容是否以预定义的前缀"机器人名称="开始。
        val botNamePrefix = "机器人名称="
        if (!event.message.contentToString().startsWith(botNamePrefix)) {
            return
        }

        // 获取完整消息内容。
        val content = event.message.contentToString()
        // 找到等号位置，用于分割前缀与新名称。
        val equalIndex = content.indexOf('=')

        // 提取新名称，去除前后空白字符。
        val newName = content.substring(equalIndex + 1).trim()
        // 若新名称为空，提示错误并退出。
        if (newName.isEmpty()) {
            event.subject.sendMessage("错误：机器人名称不能为空。")
            return
        }

        // 更新机器人的名称配置，并保存至存储。
        AdminConfig.botName = newName
        AdminConfig.save()
        logger.info("机器人名称已更新为“${newName}”")
        // 向发送者确认名称设置成功。
        event.subject.sendMessage("设置机器人名称为“${newName}”成功！")
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
        if (event.message.contentToString() == "机器人名称") {
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
    suspend fun adminSetting(event: MessageEvent) {
        if (AdminAndMasterJudge.isMaster(event) && event.message.contentToString().contains("设置管理员")) {
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
    suspend fun adminSettingQc(event: MessageEvent) {
        if (AdminAndMasterJudge.isMaster(event) && event.message.contentToString().contains("取消管理员")) {
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
    suspend fun blackListSetting(event: MessageEvent) {
        if (event.message.contentToString().contains("指令-off") || event.message.contentToString()
                .contains("指令-coff")
        ) {


            if (AdminAndMasterJudge.isAdminOrMaster(event)) {
                var s = event.message.contentToString().split("off")[1]
                if (s != "") {
                    AdminConfig.blackGroupList.add(s.replace("-", "").toLong())
                    AdminConfig.save()
                    if (event.message.contentToString().contains("coff")) {
                        event.subject.sendMessage("关闭${s}群功能：草群友")
                        return
                    }
                    event.subject.sendMessage("关闭${s}群功能：pixiv，草群友")
                } else {
                    s = event.subject.id.toString()
                    AdminConfig.blackGroupList.add(s.toLong())
                    AdminConfig.save()
                    if (event.message.contentToString().contains("coff")) {
                        event.subject.sendMessage("关闭本群群功能：草群友")
                        return
                    }
                    event.subject.sendMessage("关闭本群群功能：pixiv，草群友")
                }
            }
        }
    }

    /**
     * 去除黑名单群，master与管理都可以操作
     * @param event 可以是群或者私聊的事件
     * @author 岚雨凛<cheng_ying@outlook.com>
     */
    suspend fun blackListSettingQh(event: MessageEvent) {
        if (event.message.contentToString().contains("指令-on") || event.message.contentToString()
                .contains("指令-con")
        ) {

            if (AdminAndMasterJudge.isAdminOrMaster(event)) {
                val removeGroup = event.message.contentToString().split("on")[1]
                val groupId: Long
                if (removeGroup != "") {
                    groupId = removeGroup.replace("-", "").toLong()
                    AdminConfig.blackGroupList.remove(groupId)
                    AdminConfig.save()
                    if (event.message.contentToString().contains("con")) {
                        event.subject.sendMessage("开启${groupId}群功能：草群友")
                        return
                    }
                    event.subject.sendMessage("开启${groupId}群功能：pixiv，草群友")
                } else {
                    groupId = event.subject.id
                    AdminConfig.blackGroupList.remove(groupId)
                    AdminConfig.save()
                    if (event.message.contentToString().contains("con")) {
                        event.subject.sendMessage("开启本群群功能：草群友")
                        return
                    }
                    event.subject.sendMessage("开启本群群功能：pixiv，草群友")
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

        if (AdminAndMasterJudge.isAdminOrMaster(event) && event.message.contentToString().contains("关闭的群")) {
            val mess = MessageChainBuilder()
            mess.add("关闭功能的群列表：\n")
            for (l in AdminConfig.blackGroupList) {
                val name = event.bot.getGroup(l)?.name
                mess.add(name + "(${l})\n")
            }
            event.subject.sendMessage(mess.build())
        }
    }
}