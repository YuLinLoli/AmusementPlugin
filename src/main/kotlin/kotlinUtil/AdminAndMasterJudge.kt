package com.yulin.kotlinUtil

import com.yulin.config.AdminConfig
import net.mamoe.mirai.event.events.MessageEvent

object AdminAndMasterJudge {
    fun isMaster(event: MessageEvent): Boolean {

        return event.sender.id == AdminConfig.master
    }

    fun isAdmin(event: MessageEvent): Boolean {
        for (l in AdminConfig.adminList) {
            if (l == event.sender.id) {
                return true
            }
        }
        return false
    }

    fun isAdminOrMaster(event: MessageEvent): Boolean {

        for (l in AdminConfig.adminList) {
            if (l == event.sender.id) {
                return true
            }
        }
        return event.sender.id == AdminConfig.master
    }
}