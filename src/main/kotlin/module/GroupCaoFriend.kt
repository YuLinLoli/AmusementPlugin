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
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.at
import java.security.SecureRandom

/**
 * 草群友模块（将来可添加被草模块）
 */
object GroupCaoFriend {
    /**
     * 处理群消息事件，检查是否触发了“草群友”的指令。
     * 如果触发，根据一系列条件判断来决定执行什么操作，如选择一个群成员“草”他/她或者返回特定消息。
     *
     * @param event 表示群消息事件的数据类，包含消息内容、发送者、群成员列表等信息。
     * @suspend 此函数是一个挂起函数，可以在协程中被调用。
     */
    suspend fun cao(event: GroupMessageEvent) {
        // 检查消息内容是否为“草群友”
        if (event.message.contentToString() == "草群友") {
            // 判断是否在黑名单群中，如果是则不执行任何操作
            //判断是不是黑名单中的群
            if (BlackGroupJudge.blackGroupPd(event)) {
                logger.info("排除群，不执行草群友")
                return
            }
            // 检查发送者是否在黑名单或特定名单中，如果是则不执行任何操作
            //如果是黑名单就排除此人
            if (BlackListConfig.memberDontWantToCao.find { it.qid == event.sender.id } != null) return
            if (BlackListConfig.blackList.find { it.qid == event.sender.id } != null) return
            // 检查是否处于冷却期，如果是则不执行任何操作
            //判断是否在CD中！
            if (!caoCdPd(event)) {
                return
            }
            // 判断发送者是否为管理员或主人
            //判断是不是主人
            val tFMaster = AdminAndMasterJudge.isMaster(event)
            // 创建消息链，用于构建最终发送的消息
            //创建消息
            val message = MessageChainBuilder()
            // 获取群成员列表，过滤掉黑名单和特定成员
            val memberList = event.group.members
            val list = memberList.filterNot { r ->
                BlackListConfig.memberDontWantToCao.any { it.qid == r.id } ||
                        BlackListConfig.blackList.any { it.qid == r.id } ||
                        r.id == 2854196310L ||
                        r.id == AdminConfig.master
            }
            // 随机选择一个群成员
            //随机出一位幸运儿
            val random = SecureRandom().nextInt(list.size)
            //获取这位幸运儿
            val member = list.elementAt(random)
            // 检查是否触发了bug（选到了id为0的成员），如果是，则向管理员发送所有群成员的列表
            //bug检测（
            if (member.id == 0L) {
                for (normalMember in list) {
                    delay(370)
                    event.bot.getFriend(AdminConfig.master)!!
                        .sendMessage("${normalMember.nameCardOrNick}(${normalMember.id})")
                }
                event.bot.getFriend(AdminConfig.master)!!.sendMessage("触发bug（草到0的bug），打印了群成员列表")
            }
            // 检查是否草到了自己
            //判断是不是自己草到了自己
            val tFMe = event.sender.id == member.id
            // 获取被选中成员的头像
            //获取他的头像
            val headImage = ImageUtil.getImage("https://q1.qlogo.cn/g?b=qq&s=0&nk=${member.id}", event)
            // 开始构建消息内容
            message.add(event.sender.at())
            // 根据不同情况添加消息内容
            if (tFMe) {
                message.add(" 恭喜你和自己喜结良缘❤（自交）(群人数分之1的概率)")
                event.group.sendMessage(message.build())
                return
            }
            // 以下根据不同的概率触发不同的消息回应
            if (probability(10)) {
                message.add(" 万中无一，孤独终老。（百分之1的概率也能中快去抽卡买彩票.jpg）")
                event.group.sendMessage(message.build())
                return
            }
            if (probability(30)) {
                message.add(" 呜呜呜，只因无力(╯‵□′)╯︵┻━┻，你直接日歪了(3%概率)")
                event.group.sendMessage(message.build())
                return
            }
            if (!tFMaster && probability(30)) {
                message.add(" 该补身子了，老弟！(3%概率)")
                event.group.sendMessage(message.build())
                return
            }
            if (probability(200)) {
                message.add(" 恭喜你成功和" + member.nameCardOrNick + "(${member.id})" + "结婚了❤！(20%概率)")
                headImage?.let { it1 -> message.add(it1) }
            } else {
                message.add(" 成功草到了" + member.nameCardOrNick + "(${member.id})(73%概率)")
                headImage?.let { it1 -> message.add(it1) }
            }
            // 发送最终构建的消息
            event.group.sendMessage(message.build())
        }
    }

    /**
     * 处理群消息事件，检查并处理“草群友”命令的冷却时间。
     *
     * @param event 群消息事件对象。
     * @return 如果命令可以执行返回true，否则返回false。
     *
     * 此函数用于接收群消息事件，并检查发件人是否有权限执行“草群友”命令，
     * 以及检查该命令是否处于冷却时间中。如果命令可以执行，则记录执行时间并保存配置，
     * 如果命令处于冷却时间中，则通知发件人需要等待的时间。
     */
    private suspend fun caoCdPd(event: GroupMessageEvent): Boolean {
        // 获取当前时间戳
        val timeMillis = System.currentTimeMillis()
        // 获取群ID和发送者QQ号
        //群id
        val gid = event.group.id
        //qq
        val qid = event.sender.id

        // 检查发送者是否为管理员或主人，如果是则可以直接执行命令
        //判断是否为主人发的
        if (AdminAndMasterJudge.isMaster(event)) {
            return true
        }

        // 从配置中查找对应群组的发送者列表
        val groupSenderList = CdConfig.groupSender.find { it.gid == gid }

        // 如果是首次使用命令或配置中没有找到对应群组的记录，则初始化发送者列表并保存配置
        //第一次创建文件！
        if (CdConfig.groupSender.size == 0 || groupSenderList == null) {
            val list: MutableList<Sender> = mutableListOf()
            list.add(Sender(qid, timeMillis))
            CdConfig.groupSender.add(GroupSender(gid, list))
            CdConfig.save()
            return true
        }

        // 在群组的发送者列表中查找指定QQ号的发送者记录
        val sender = groupSenderList.sender.find { it.qid == qid }

        // 如果找不到发送者记录，则添加新的发送者记录并保存配置
        if (sender == null) {
            groupSenderList.sender.add(Sender(qid, timeMillis))
            CdConfig.save()
            return true
        }

        // 如果当前时间与上次执行命令的时间间隔大于1小时，则重置冷却时间并保存配置
        if (timeMillis - sender.cd > 3600000) {
            sender.cd = timeMillis
            CdConfig.save()
            return true
        }

        // 计算命令的剩余冷却时间，并发送消息通知发件人
        val times = (3600000 - (timeMillis - sender.cd)) / 1000
        logger.info("CD还有${times}")
        event.group.sendMessage("请等待${times}秒后再使用“草群友”指令！！")
        return false
    }

}