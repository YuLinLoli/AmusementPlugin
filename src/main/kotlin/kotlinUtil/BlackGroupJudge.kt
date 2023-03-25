package com.mirai.kotlinUtil

import com.mirai.config.AdminConfig
import net.mamoe.mirai.event.events.GroupMessageEvent

object BlackGroupJudge {

    /**
     * 判断是否是黑名单的群
     * @return ture or false
     */
    fun blackGroupPd(event:GroupMessageEvent): Boolean{
        var tFBlackGroupList = false
        for (l in AdminConfig.blackGroupList) {
            if (event.group.id == l){
                tFBlackGroupList = true
            }
        }
        return tFBlackGroupList
    }
}