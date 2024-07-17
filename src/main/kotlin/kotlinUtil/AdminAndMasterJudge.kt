package com.yulin.kotlinUtil

import com.yulin.config.AdminConfig
import net.mamoe.mirai.event.events.MessageEvent

/**
 * 判断是否为管理员或主人的工具类。
 * 该类提供了一系列方法来检查事件发送者是否是管理员或主人。
 */
object AdminAndMasterJudge {

    /**
     * 判断事件发送者是否为主人。
     *
     * @param event 事件对象，包含发送者信息。
     * @return 如果发送者是主人，则返回true；否则返回false。
     */
    fun isMaster(event: MessageEvent): Boolean = event.sender.id == AdminConfig.master

    /**
     * 判断事件发送者是否为管理员。
     *
     * @param event 事件对象，包含发送者信息。
     * @return 如果发送者是管理员之一，则返回true；否则返回false。
     */
    fun isAdmin(event: MessageEvent): Boolean = AdminConfig.adminList.contains(event.sender.id)

    /**
     * 判断事件发送者是否为管理员或主人。
     *
     * @param event 事件对象，包含发送者信息。
     * @return 如果发送者是管理员或主人之一，则返回true；否则返回false。
     */
    fun isAdminOrMaster(event: MessageEvent): Boolean = isMaster(event) || isAdmin(event)

}
