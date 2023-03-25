package com.mirai.module


import com.mirai.config.AdminConfig
import com.mirai.kotlinUtil.BlackGroupJudge
import com.mirai.kotlinUtil.ImageUtil
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChainBuilder
import java.util.*

/**
 * 草群友模块（将来可添加被草模块）
 */
object GroupCaoFriend {
    /**
     * 草群友
     * @param event 群消息事件
     *
     */
    suspend fun cao(event: GroupMessageEvent) {
        if (event.message.contentToString() == "草群友") {
            //主人id
            val master = AdminConfig.master
            //判断是不是主人
            val tFMaster = event.sender.id == master
            //判断是不是黑名单中的群
            val tFBlackGroupList = BlackGroupJudge.blackGroupPd(event)
            //创建消息
            val message = MessageChainBuilder()
            //创建判断用的随机数
            val randomPd = Random().nextInt(100)
            //不是黑名单的群就继续执行
            if (!tFBlackGroupList){
                val list = event.group.members
                val size = list.size
                //随机出一位幸运儿
                val random = Random().nextInt(size - 1)
                //获取这位幸运儿
                val member = list.elementAt(random)
                //判断是不是自己草到了自己
                val tFMe = event.sender.id == member.id
                //获取他的头像
                val headImage = ImageUtil.getImage("https://q1.qlogo.cn/g?b=qq&s=0&nk=${member.id}", event)
                message.add(At(event.sender.id))
                if (!tFMe) {
                    if (!tFMaster && randomPd < 1) {
                        message.add(" 万中无一，孤独终老。（百分之1的概率也能中快去抽卡买彩票.jpg）")
                    } else if (!tFMaster && randomPd < 6) {
                        message.add(" 呜呜呜，只因无力(╯‵□′)╯︵┻━┻，你直接日歪了，细狗")
                    } else if (randomPd <= 75) {
                        message.add(" 成功草到了" + member.nameCardOrNick + "(${member.id})")

                        headImage?.let { it1 -> message.add(it1) }
                    }else if (randomPd <= 95){
                        message.add(" 恭喜你和" + member.nameCardOrNick + "(${member.id})" + "喜结良缘❤")
                        headImage?.let { it1 -> message.add(it1) }
                    }
                }else if (!tFMaster){
                    message.add(" 恭喜你和自己喜结良缘❤（自交）")
                }else{
                    message.add(" 恭喜主人和枫喜结良缘❤")
                }
                event.group.sendMessage(message.build())

            }else{
                println("排除群，不执行草群友指令！")
            }
        }
    }
}