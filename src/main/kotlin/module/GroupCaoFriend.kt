package com.yulin.module


import com.yulin.AmusementPlugin.logger
import com.yulin.AmusementPlugin.save
import com.yulin.config.AdminConfig
import com.yulin.config.CdConfig
import com.yulin.kotlinUtil.BlackGroupJudge
import com.yulin.kotlinUtil.ImageUtil
import com.yulin.pojo.GroupSender
import com.yulin.pojo.Sender
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
            //判断是否在CD中！
            if (!caoCdPd(event)) {
                return
            }
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
                var i = "0"
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
                        i = "孤独终老"
                        message.add(" 万中无一，孤独终老。（百分之1的概率也能中快去抽卡买彩票.jpg）")
                    } else if (!tFMaster && randomPd < 6) {
                        i = "草歪了"
                        message.add(" 呜呜呜，只因无力(╯‵□′)╯︵┻━┻，你直接日歪了，细狗")
                    } else if (randomPd <= 75  && member.id != master) {
                        i = "正常草到了"
                        message.add(" 成功草到了" + member.nameCardOrNick + "(${member.id})")
                        headImage?.let { it1 -> message.add(it1) }
                    } else if (randomPd <= 95 && member.id != master) {
                        i = "正常喜结良缘"
                        message.add(" 恭喜你和" + member.nameCardOrNick + "(${member.id})" + "喜结良缘❤")
                        headImage?.let { it1 -> message.add(it1) }
                    } else {
                        i = "就凭你也想草别人？不赶紧补补身子，别让别人笑话你肾虚"
                        message.add(" 就凭你也想草别人？不赶紧补补身子，别让别人笑话你肾虚!")
                    }
                } else if (!tFMaster) {
                    message.add(" 恭喜你和自己喜结良缘❤（自交）")
                } else {
                    message.add(" 恭喜主人和${event.bot.nameCardOrNick}喜结良缘❤")
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

            }else{
                logger.info("排除群，不执行草群友指令！")
            }
        }
    }

    private suspend fun caoCdPd(event: GroupMessageEvent): Boolean {
        val timeMillis = System.currentTimeMillis()
        //群id
        val gid = event.group.id
        //qq
        val qid = event.sender.id
        //判断配置文件内是否有该群
        var gTF = false
        //判断是否有该群成员
        var qTf = false
        //最后的返回值！
        var CD = false
        //如果有CD就设值！
        var cdTime = 0L
        //判断是否为主人发的
        if (event.sender.id != AdminConfig.master) {
            return true
        }
        //第一次创建文件！
        if (CdConfig.groupSender.isEmpty()) {
            val list: MutableList<Sender> = listOf<Sender>().toMutableList()
            list.add(Sender(qid, timeMillis))
            CdConfig.groupSender.add(GroupSender(gid, list))
            CdConfig.save()
            return true
        }
        //判断是否在CD中
        for (g in CdConfig.groupSender) {
            if (g.gid == gid) {
                gTF = true
                for (q in g.sender) {
                    //如果能找到此群成员对象就赋予CD！并让流程继续执行
                    if (qid == q.qid) {
                        //如果进去了就设置新的CD
                        if (timeMillis - q.cd > AdminConfig.cdTime * 1000) {
                            q.cd = timeMillis
                            CdConfig.save()
                            CD = true

                        }
                        cdTime = q.cd
                        qTf = true
                    }
                }
            }

        }
        //判断是否有该群，没有就创建群把成员CD加入
        if (!gTF) {
            val list: MutableList<Sender> = listOf<Sender>().toMutableList()
            list.add(Sender(qid, timeMillis))
            CdConfig.groupSender.add(GroupSender(gid, list))
            CdConfig.save()
            return true
        }
        //判断是否有该群友，没有就加入
        if (!qTf) {
            for (g in CdConfig.groupSender) {
                if (g.gid == gid) {
                    g.sender.add(Sender(qid, timeMillis))
                    CdConfig.save()
                    return true
                }
            }
        }
        //如果在CD就回复在CD
        if (!CD) {
            val times = (3600000 - (timeMillis - cdTime)) / 1000
            logger.info("${event.sender.nameCardOrNick}(${event.sender.id})的CD还有${times}秒")
            event.group.sendMessage("请等待${times}秒后再使用“草群友”指令！！")
            return false
        }
        return true
    }
}