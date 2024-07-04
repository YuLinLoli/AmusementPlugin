package com.yulin


import com.yulin.cg.BuildConfig
import com.yulin.config.AdminConfig
import com.yulin.config.CdConfig
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
        CdConfig.reload()
    }


    override fun onEnable() {
        CdConfig.reload()
        AdminConfig.reload()

        //监听群消息
        globalEventChannel().subscribeAlways<GroupMessageEvent> {

            help(this)
            //草群友插件执行
            cao(this)
            //设置插件管理员执行
            adminSetting(this)
            adminSettingQc(this)
            //设置群黑名单执行
            blackListSetting(this)
            blackListSettingQh(this)
            cdEdit(this)
        }
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            help(this)
            //设置插件管理员执行
            adminSetting(this)
            adminSettingQc(this)
            //设置群黑名单执行
            blackListShow(this)
            blackListSetting(this)
            blackListSettingQh(this)
            cdEdit(this)

        }
        AdminConfig.save()
        CdConfig.save()

    }

}



