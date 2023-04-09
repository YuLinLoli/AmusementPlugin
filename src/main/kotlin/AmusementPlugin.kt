package com.mirai


import com.mirai.config.AdminConfig
import com.mirai.config.CdConfig
import com.mirai.config.GroupImageConfig
import com.mirai.module.AdminConfigEdit.adminSetting
import com.mirai.module.AdminConfigEdit.adminSettingQc
import com.mirai.module.AdminConfigEdit.blackListSetting
import com.mirai.module.AdminConfigEdit.blackListSettingQh
import com.mirai.module.AdminConfigEdit.blackListShow
import com.mirai.module.GroupCaoFriend.cao
import com.mirai.module.GroupImageEdit.Companion.imageAdd
import com.mirai.module.GroupImageEdit.Companion.sendImage
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMemberEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel


object AmusementPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "com.mirai.AmusementPlugin",
        name = "娱乐插件（有很多小的娱乐功能哦）",
        version = "1.0.3",
    )
) {
    override fun PluginComponentStorage.onLoad() {
        AdminConfig.reload()
        GroupImageConfig.reload()
        CdConfig.reload()
    }


    override fun onEnable() {
        AdminConfig.reload()
        AdminConfig.save()
        GroupImageConfig.reload()
        GroupImageConfig.save()
        CdConfig.reload()
        CdConfig.save()
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
            //添加表情图片执行
            val b = imageAdd(this)
            if (!b){
                sendImage(this)
            }

        }
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            //设置插件管理员执行
            adminSetting(this)
            adminSettingQc(this)
            //设置群黑名单执行
            blackListSetting(this)
            blackListSettingQh(this)
            blackListShow(this)
        }
        globalEventChannel().subscribeAlways<GroupMemberEvent> { }


    }

}



