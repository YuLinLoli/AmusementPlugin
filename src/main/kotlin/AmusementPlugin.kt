package com.yulin


import com.yulin.cg.BuildConfig
import com.yulin.config.AdminConfig
import com.yulin.module.AdminConfigEdit.adminSetting
import com.yulin.module.AdminConfigEdit.adminSettingQc
import com.yulin.module.AdminConfigEdit.blackListSetting
import com.yulin.module.AdminConfigEdit.blackListSettingQh
import com.yulin.module.AdminConfigEdit.blackListShow
import com.yulin.module.AdminConfigEdit.cdEdit
import com.yulin.module.GroupCaoFriend.cao
import com.yulin.module.Help.help
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.FriendMessageEvent

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel


object AmusementPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = BuildConfig.id,
        name = BuildConfig.name,
        version = BuildConfig.yulinVersion
    )
) {
    override fun PluginComponentStorage.onLoad() {
        AdminConfig.reload()
    }


    override fun onEnable() {
        AdminConfig.reload()
        AdminConfig.save()
        //监听群消息
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            //草群友插件执行
            cao(this)
            //设置插件管理员执行
            adminSetting(this)
            adminSettingQc(this)
            //设置群黑名单执行
            blackListSetting(this)
            blackListSettingQh(this)
            cdEdit(this)
            help(this)
        }
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            //设置插件管理员执行
            adminSetting(this)
            adminSettingQc(this)
            //设置群黑名单执行
            blackListSetting(this)
            blackListSettingQh(this)
            blackListShow(this)
            cdEdit(this)
            help(this)
        }


    }

}



