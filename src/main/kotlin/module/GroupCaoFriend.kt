package com.yulin.module


import com.yulin.AmusementPlugin.logger
import com.yulin.AmusementPlugin.save
import com.yulin.config.AdminConfig
import com.yulin.config.CdConfig
import com.yulin.kotlinUtil.BlackGroupJudge
import com.yulin.kotlinUtil.ImageUtil
import com.yulin.pojo.GroupSender
import com.yulin.pojo.Sender
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChainBuilder
import java.security.SecureRandom

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
            //判断是不是黑名单中的群
            if (BlackGroupJudge.blackGroupPd(event)) {
                logger.info("排除群，不执行草群友指令！")
                return
            }
            //判断是否在CD中！
            if (!caoCdPd(event)) {
                return
            }

            //创建消息
            val message = MessageChainBuilder()
            //创建判断事件用的随机数
            val randomPd = SecureRandom().nextInt(100)
            var i = ""
            val list = event.group.members
            var member: NormalMember
            list.remove(2854196310L)
            list.remove(AdminConfig.master)
            val size = list.size
            //判断群友的随机数
            val random = SecureRandom().nextInt(size)
            //获取这位幸运儿
            member = list.elementAt(random)
            //bug处理（
            if (member.id == 0L) {
                list.remove(member.id)
                member = list.elementAt(random - 1)
            }
            //判断是不是自己草到了自己
            val tFMe = event.sender.id == member.id
            //获取他的头像
            val headImage = ImageUtil.getImage("https://q1.qlogo.cn/g?b=qq&s=0&nk=${member.id}", event)
            message.add(At(event.sender.id))
            //如果没有草到自己
            if (!tFMe) {
                //不是主人且值小于1
                if (randomPd < 1) {
                    i = "孤独终老"
                    message.add(" 万中无一，孤独终老。（百分之1的概率也能中快去抽卡买彩票.jpg）")
                } else if (randomPd < 6) {
                    i = "草歪了"
                    message.add(" 呜呜呜，只因无力(╯‵□′)╯︵┻━┻，你直接日歪了，细狗")
                } else if (randomPd <= 75) {
                    i = "正常草到了"
                    message.add(" 成功草到了" + member.nameCardOrNick + "(${member.id})")
                    headImage?.let { it1 -> message.add(it1) }
                } else if (randomPd <= 95) {
                    i = "正常喜结良缘"
                    message.add(" 恭喜你和" + member.nameCardOrNick + "(${member.id})" + "喜结良缘❤")
                    headImage?.let { it1 -> message.add(it1) }
                } else {
                    i = "就凭你也想草别人？不赶紧补补身子，别让别人笑话你肾虚"
                    message.add(" 该补身子了，老弟！")
                }
            } else {
                message.add(" 恭喜你和自己喜结良缘❤（自交）")
            }
            val test = MessageChainBuilder()
            test.add(At(event.sender.id))
            if (message == test) {
                event.bot.getFriend(AdminConfig.master)
                    ?.sendMessage(
                        "发现有人触发空消息体的bug！\n" +
                                "触发指令的人是：${event.sender.nameCardOrNick}(${event.sender.id})\n" +
                                "被草的人是：${member.nameCardOrNick}(${member.id})\n" +
                                "触发的指令为：${i}"
                    )
            }
            event.group.sendMessage(message.build())

        }
    }


    private suspend fun caoCdPd(event: GroupMessageEvent): Boolean {
        val timeMillis = System.currentTimeMillis()
        //群id
        val gid = event.group.id
        //qq
        val qid = event.sender.id
        //判断是否为主人发的
        if (qid == AdminConfig.master) {
            return true
        }
        val groupSenderList = CdConfig.groupSender.find { it.gid == gid }
        //第一次创建文件！
        if (CdConfig.groupSender.size == 0 || groupSenderList == null) {
            val list: MutableList<Sender> = mutableListOf()
            list.add(Sender(qid, timeMillis))
            CdConfig.groupSender.add(GroupSender(gid, list))
            CdConfig.save()
            return true
        }
        val sender = groupSenderList.sender.find { it.qid == qid }
        if (sender == null) {
            groupSenderList.sender.add(Sender(qid, timeMillis))
            CdConfig.save()
            return true
        }
        if (timeMillis - sender.cd > AdminConfig.cdTime * 1000) {
            sender.cd = timeMillis
            CdConfig.save()
            return true
        }
        val times = (AdminConfig.cdTime * 1000 - (timeMillis - sender.cd)) / 1000
        logger.info("CD还有${times}")
        event.group.sendMessage("请等待${times}秒后再使用“草群友”指令！！")
        return false
    }
}