package com.mirai.module

import com.mirai.AmusementPlugin.save
import com.mirai.config.AdminConfig
import com.mirai.config.GroupImageConfig
import com.mirai.kotlinUtil.ImageAddUtil
import com.mirai.kotlinUtil.ImageUtil
import com.mirai.kotlinUtil.ImageUtil.Companion.loadImage
import com.mirai.pojo.GroupImage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageChainBuilder

class GroupImageEdit {
    companion object {
        //添加表情
        suspend fun imageAdd(event: GroupMessageEvent): Boolean {
            val mess = event.message.contentToString()
            var image: Image? = null
            val imageUrl: String
            var imageName = ""
            var saveTf = false
            val groupImage = GroupImage(0L, true, arrayListOf())
            groupImage.groupId = event.group.id
            if (!mess.startsWith(GroupImageConfig.groupImageAd)){
                return false
            }
            //判断是否有添加指令，并且判断是否为主人！然后取出图片以及图片名称
            if (event.sender.id == AdminConfig.master) {
                for (m in event.message) {
                    if (m.contentToString().startsWith(GroupImageConfig.groupImageAd)) {
                        imageName = m.contentToString().split(GroupImageConfig.groupImageAd)[1].replace("\n","")
                    }
                    if (m.toString().contains("image")) {
                        image = m as Image
                        imageName += m.toString().split("}")[1].split(",")[0]

                    }
                }
            }

            //获取图片的URL并保存图片的文件
            if (image != null) {
                imageUrl = image.queryUrl()
                saveTf = ImageUtil.saveImage(imageUrl, imageName)
            }
            if (!saveTf) {
                event.group.sendMessage("添加失败！图片保存失败！")
                return false
            }
            when (ImageAddUtil.imageAdd(groupImage, GroupImageConfig, imageName)) {
                1 -> {
                    event.group.sendMessage("添加成功！")
                    return true
                }

                0 -> {
                    event.group.sendMessage("添加失败！原因未知")
                    return false
                }

                11 -> {
                    event.group.sendMessage("添加失败！已经添加过该表情了")
                    return false
                }

                10 -> {
                    groupImage.imageList.add(imageName)
                    GroupImageConfig.groupImage.add(groupImage)
                    GroupImageConfig.save()
                    event.group.sendMessage("本群首次保存图片，添加表情成功！")
                    return true
                }

                else -> return false
            }


        }

        //读取表情并发送
        suspend fun sendImage(event: GroupMessageEvent): Boolean {


            var groupImage = GroupImage(0L, true, arrayListOf())
            //判断该群是否有记录，如果有记录则判断该群的开关是否开启，如果开启则给定对象！
            for (image1 in GroupImageConfig.groupImage) {
                if (image1.groupId == event.group.id && image1.tf) {
                    groupImage = image1
                }
            }
            if (groupImage.groupId == 0L) {
                return false
            }
            val imageList = groupImage.imageList
            for (s in imageList) {
                if (event.message.contentToString().contains(s.split(".")[0])) {
                    val image = loadImage(event, s)
                    val builder = MessageChainBuilder()
                    if (image != null) {
                        builder.add(image)
                        event.group.sendMessage(builder.build())
                        return true
                    }
                }
            }



            return false
        }


        //读取表情
    }
}