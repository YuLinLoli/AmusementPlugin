package com.yulin


import com.yulin.config.AdminConfig
import com.yulin.config.BlackListConfig
import com.yulin.config.CdConfig
import com.yulin.config.GroupImageConfig
import com.yulin.module.AdminConfigEdit.adminSetting
import com.yulin.module.AdminConfigEdit.adminSettingQc
import com.yulin.module.AdminConfigEdit.blackListSetting
import com.yulin.module.AdminConfigEdit.blackListSettingQh
import com.yulin.module.AdminConfigEdit.blackListShow
import com.yulin.module.BlackListEdit.blackListMain
import com.yulin.module.GroupCaoFriend.cao
import com.yulin.module.GroupImageEdit.Companion.imageAdd
import com.yulin.module.GroupImageEdit.Companion.sendImage
import com.yulin.module.MessageUtil.recallMessage
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel


object AmusementPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "com.yulin.AmusementPlugin",
        name = "娱乐插件（有很多小的娱乐功能哦）",
        version = "1.1.1",
    )
) {
    override fun PluginComponentStorage.onLoad() {
        AdminConfig.reload()
        GroupImageConfig.reload()
        CdConfig.reload()
        BlackListConfig.reload()
    }


    override fun onEnable() {
        AdminConfig.reload()
        GroupImageConfig.reload()
        CdConfig.reload()
        BlackListConfig.reload()
        //监听群消息
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            //黑名单插件执行
            blackListMain(this)
            //撤回被引用的消息
            recallMessage(this)
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
            //黑名单插件执行
            blackListMain(this)
            //设置插件管理员执行
            adminSetting(this)
            adminSettingQc(this)
            //设置群黑名单执行
            blackListSetting(this)
            blackListSettingQh(this)
            blackListShow(this)
        }
        globalEventChannel().subscribeAlways<GroupEvent> {

        }
        CdConfig.save()
        GroupImageConfig.save()
        AdminConfig.save()
        BlackListConfig.save()
    }

}



