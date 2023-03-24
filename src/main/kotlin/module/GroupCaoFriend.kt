package com.mirai.module


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
    suspend fun cao(admin: Long, groupsArr: Array<Long>, event: GroupMessageEvent) {
        if (event.message.contentToString() == "草群友") {
            var b = true
            for (g in groupsArr) {
                if (g == event.group.id) {
                    b = false
                }
            }
            if (b) {
                val message = MessageChainBuilder()
                message.add(At(event.sender.id))
                val list = event.group.members
                val size = list.size
                val random = Random().nextInt(size - 1)
                val randomPd = Random().nextInt(100)
                val member = list.elementAt(random)
//                    val member = list[random.toLong()]
                val headImage = ImageUtil.getImage("https://q1.qlogo.cn/g?b=qq&s=0&nk=${member.id}", event)
                if (member.id != admin) {
                    if (randomPd < 1) {
                        message.add(" 万中无一，孤独终老。（百分之1的概率也能中快去抽卡买彩票.jpg）")
                    } else if (randomPd < 6) {
                        message.add(" 呜呜呜，只因无力(╯‵□′)╯︵┻━┻，你直接日歪了，细狗")
                    } else if (randomPd <= 75) {
                        message.add(" 成功草到了" + member.nameCardOrNick + "(${member.id})")

                        headImage?.let { it1 -> message.add(it1) }
                    } else {
                        if (event.sender.id == member.id) {
                            message.add(" 恭喜你和自己喜结良缘❤（自交）")
                        } else {
                            message.add(" 恭喜你和" + member.nameCardOrNick + "(${member.id})" + "喜结良缘❤")
                            headImage?.let { it1 -> message.add(it1) }
                        }
                    }
                } else if (member.id == admin && event.sender.id != admin) {
                    message.add(" 就凭你也想草我主人，做梦去吧！")
                } else if (member.id == event.sender.id) {
                    message.add(" 恭喜你和自己喜结良缘❤")
                }
                event.group.sendMessage(message.build())
            } else {
                println("排除群，不执行草群友指令！")
            }

        }
    }
}