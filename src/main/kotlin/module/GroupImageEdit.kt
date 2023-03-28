package com.mirai.module

import com.mirai.AmusementPlugin.save
import com.mirai.config.AdminConfig
import com.mirai.config.GroupImageConfig
import com.mirai.kotlinUtil.ImageUtil
import com.mirai.pojo.GroupImage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl

class GroupImageEdit {
    companion object {
        //添加表情
        suspend fun imageAdd(event: GroupMessageEvent) {
            val mess = event.message.contentToString()
            var image: Image? = null
            val imageUrl: String
            var str: String
            var imageName = ""
            if (mess.contains("添加")) {
                for (m in event.message) {
                    if (m.contentToString().contains("添加")) {
                        str = m.contentToString().split("加")[1]
                        println(str)
                    }
                    if (m.contentToString().contains("image")) {
                        image = m as Image
                        imageName = image.contentToString().split("{")[1].split("}")[0]
                        println(imageName)
                    }
                }
            }
            //获取图片的URL
            if (AdminConfig.master == event.sender.id) {
                var b = true
                if (image != null) {
                    imageUrl = image.queryUrl()
                    println(imageUrl)

                    ImageUtil.saveImage(imageUrl, imageName)
                }
                val group = GroupImageConfig.groupImage
                for (g in group) {
                    if (g.groupId == event.group.id) {
                        g.imageList.add(imageName)
                        event.group.sendMessage("添加表情成功！")
                        b = false
                    }
                }
                if (b) {
                    val ml: MutableList<String> = arrayListOf()
                    ml.add(imageName)
                    group.add(GroupImage(event.group.id, true, ml))
                }
            }
            GroupImageConfig.save()

        }


        //读取表情
    }
}