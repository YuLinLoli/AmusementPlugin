package com.yulin.module


import com.yulin.AmusementPlugin.logger
import com.yulin.AmusementPlugin.save
import com.yulin.config.AdminConfig
import com.yulin.config.BlackListConfig
import com.yulin.config.CdConfig
import com.yulin.kotlinUtil.AdminAndMasterJudge
import com.yulin.kotlinUtil.BlackGroupJudge
import com.yulin.kotlinUtil.ImageUtil
import com.yulin.kotlinUtil.MathUtil.Companion.probability
import com.yulin.pojo.GroupSender
import com.yulin.pojo.Sender
import kotlinx.coroutines.delay
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.at
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
            //判断是不是黑名单中的群
            if (BlackGroupJudge.blackGroupPd(event)) {
                logger.info("排除群，不执行草群友")
                return
            }
            for (s in BlackListConfig.memberDontWantToCao) {
                if (s.qid == event.sender.id) {
                    return
                }
            }
            //如果是黑名单就排除此人
            if (BlackListConfig.blackList.contains(event.sender.id)) return
            //判断是否在CD中！
            if (!caoCdPd(event)) {
                return
            }
            var botName = AdminConfig.botName
            if (botName == "") {
                botName = event.bot.nameCardOrNick
            }
            //判断是不是主人
            val tFMaster = AdminAndMasterJudge.isMaster(event)

            //创建消息
            val message = MessageChainBuilder()

            val list = event.group.members
                val size = list.size
                var member: NormalMember
                //排除主人
                while (true) {
                    //随机出一位幸运儿
                    val random = Random().nextInt(size)
                    //获取这位幸运儿
                    member = list.elementAt(random)
                    //bug检测（
                    if (member.id == 0L) {
                        for (normalMember in list) {
                            delay(370)
                            event.bot.getFriend(AdminConfig.master)!!
                                .sendMessage("${normalMember.nameCardOrNick}(${normalMember.id})")
                        }
                        event.bot.getFriend(AdminConfig.master)!!.sendMessage("触发bug（草到0的bug），打印了群成员列表")
                    }
                    var bbb = false
                    for (s in BlackListConfig.memberDontWantToCao) {
                        if (s.qid == member.id) {
                            bbb = true
                        }
                    }
                    if (bbb) continue
                    //如果是黑名单的qq，则排除他
                    if (BlackListConfig.blackList.contains(member.id)) continue
                    //如果是主人自己触发的指令，则跳出循环（如果主人草到自己在下面会处理为“恭喜主人和botName喜结良缘❤”）
                    if (tFMaster) {
                        break
                    }
                    //如果幸运儿的ID不是主人的ID的话就继续执行程序，否则继续循环新的幸运儿
                    if (member.id != AdminConfig.master) {
                        break
                    }

                }

                //判断是不是自己草到了自己
                val tFMe = event.sender.id == member.id
                //获取他的头像
                val headImage = ImageUtil.getImage("https://q1.qlogo.cn/g?b=qq&s=0&nk=${member.id}", event)
                message.add(event.sender.at())

            if (tFMaster && tFMe) {
                    message.add(" ${botName}最喜欢主人啦！❤")
                    event.group.sendMessage(message.build())
                    return
                }
            if (tFMe) {
                message.add(" 恭喜你和自己喜结良缘❤（自交）(群人数分之1的概率)")
                event.group.sendMessage(message.build())
                return
            }
                if (!tFMaster && probability(10)) {
//                  i = "孤独终老"
                    message.add(" 万中无一，孤独终老。（百分之1的概率也能中快去抽卡买彩票.jpg）")
                    event.group.sendMessage(message.build())
                    return
                }
                if (!tFMaster && probability(30)) {
//                  i = "草歪了"
                    message.add(" 呜呜呜，只因无力(╯‵□′)╯︵┻━┻，你直接日歪了(3%概率)")
                    event.group.sendMessage(message.build())
                    return
                }
                if (!tFMaster && probability(30)) {
//                  i = "就凭你也想草别人？不赶紧补补身子，别让别人笑话你肾虚"
                    message.add(" 该补身子了，老弟！(3%概率)")
                    event.group.sendMessage(message.build())
                    return
                }
                if (probability(200)) {
//                  i = "正常喜结良缘"
                    message.add(" 恭喜你成功和" + member.nameCardOrNick + "(${member.id})" + "结婚了❤！(20%概率)")
                    headImage?.let { it1 -> message.add(it1) }
                }else {
//                  i = "正常草到了"
                    message.add(" 成功草到了" + member.nameCardOrNick + "(${member.id})(73%概率)")
                    headImage?.let { it1 -> message.add(it1) }
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
        //判断配置文件内是否有该群
        var gTF = false
        //判断是否有该群成员
        var qTf = false
        //最后的返回值！
        var CD = false
        //如果有CD就设值！
        var cdTime = 0L
        //判断是否为主人发的
        if (AdminAndMasterJudge.isMaster(event)) {
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
                        if (timeMillis - q.cd > 3600000) {
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
            println("CD还有${times}")
            event.group.sendMessage("请等待${times}秒后再使用“草群友”指令！！")
            return false
        }
        return true
    }
}