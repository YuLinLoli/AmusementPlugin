package com.mirai


import com.mirai.module.GroupCaoFriend.cao
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel


object AmusementPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "com.mirai.AmusementPlugin",
        name = "娱乐插件（有很多小的娱乐功能哦）",
        version = "1.0.0",
    )
) {
    override fun PluginComponentStorage.onLoad() {
        //测试用，导出插件的时候请注释掉！！！
//        if (SemVersion.parseRangeRequirement("<= 2.14.0").test(MiraiConsole.version)) {
//            logger.warning { "Mirai版本低于预期，将升级协议版本" }
//            FixProtocolVersion.update()
//        }
    }

    @JvmStatic
    //此处可填写机器人主人的qq，让群员们草不到你
    val admin = 123456L

    @JvmStatic
    //此处可加载文件并填写黑名单
    var gruops = arrayOf(123456L)

    override fun onEnable() {
        //监听群消息
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            //草群友插件执行
            cao(admin, gruops, this)
        }


    }

}



