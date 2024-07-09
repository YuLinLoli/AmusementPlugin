package com.yulin.kotlinUtil

import com.yulin.config.AdminConfig
import net.mamoe.mirai.event.events.GroupMessageEvent

object BlackGroupJudge {

    /**
     * 判断是否是黑名单的群
     * @return ture or false
     */
    fun blackGroupPd(event: GroupMessageEvent): Boolean {
        return AdminConfig.blackGroupList.contains(event.group.id)
    }

}