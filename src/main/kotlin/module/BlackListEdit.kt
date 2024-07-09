package com.yulin.module

import com.yulin.AmusementPlugin.save
import com.yulin.config.BlackListConfig
import com.yulin.kotlinUtil.AdminAndMasterJudge.isAdminOrMaster
import com.yulin.module.MessageUtil.currentTimestampToDay
import com.yulin.pojo.BlackSender
import com.yulin.pojo.Sender
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.findIsInstance

object BlackListEdit {
    suspend fun blackListMain(e: MessageEvent) {
        if (!isAdminOrMaster(e)) {
            return
        }
        add(e)
        del(e)
        show(e)
    }

    private suspend fun add(event: MessageEvent) {
        val contentToString = event.message.contentToString()
        if (contentToString.startsWith("加黑")) {
            //把加黑的qq分割出来“好友加黑123456-人品不行”
            val split = contentToString.split("黑")[1].split("-")
            BlackListConfig.blackList.add(BlackSender(split[0].toLong(), split[1]))
            BlackListConfig.save()
            event.subject.sendMessage("添加${split[0].toLong()}黑名单成功！")
        }
    }

    suspend fun memberAdd(event: MessageEvent) {
        if (event.message.contentToString() == "我以后不想被草也不想草别人了") {
            val sender = BlackListConfig.memberDontWantToCao.find { it.qid == event.sender.id }
            if (sender != null) {
                event.subject.sendMessage("你已经被添加黑名单！")
                return
            }
            BlackListConfig.memberDontWantToCao.add(Sender(event.sender.id, System.currentTimeMillis()))
            BlackListConfig.save()
            event.subject.sendMessage("已经添加黑名单，以后都不会被草了(也不能草别人了)！")
        }
    }

    suspend fun memberDel(event: MessageEvent) {
        if (event.message.contentToString() == "我想被草了") {
            val sender = BlackListConfig.memberDontWantToCao.find { it.qid == event.sender.id }
            if (sender == null) {
                event.subject.sendMessage("你没有被添加黑名单，不能删除！")
                return
            }
            if (System.currentTimeMillis() - sender.cd > 2678400000L) {
                BlackListConfig.memberDontWantToCao.remove(sender)
                event.subject.sendMessage("已经删除黑名单，以后都会被草了！")
                BlackListConfig.save()
                return
            }
            val currentTimestampToDay =
                currentTimestampToDay(2678400000L - (System.currentTimeMillis() - sender.cd))
            event.subject.sendMessage("还剩下${currentTimestampToDay}才可以解除黑名单！")
            return
        }

    }

    private suspend fun del(event: MessageEvent) {
        val contentToString = event.message.contentToString()
        if (!contentToString.startsWith("去黑")) {
            return
        }
        if (BlackListConfig.blackList.size == 0) {
            event.subject.sendMessage("黑名单列表为空无法删除！")
            return
        }
        val at = event.message.findIsInstance<At>()
        if (at != null) {
            if (BlackListConfig.blackList.find { it.qid == at.target } == null) {
                event.subject.sendMessage("你删个屁，${at.target}没有在黑名单中！")
                return
            }

            if (BlackListConfig.blackList.removeIf { it.qid == at.target }) {
                BlackListConfig.save()
                event.subject.sendMessage("删除" + at.target + "黑名单成功！")
            }
            return
        }

        val l = event.message.contentToString().split("黑")[1].toLong()
        val sender = BlackListConfig.blackList.find { it.qid == l }
        if (sender == null) {
            event.subject.sendMessage("删除失败，请检查qq是否正确！")
            return
        }
        if (BlackListConfig.blackList.remove(sender)) {
            BlackListConfig.save()
            event.subject.sendMessage("删除" + l + "黑名单成功！")
            return
        }

    }

    private suspend fun show(event: MessageEvent) {
        if (event.message.contentToString() != "黑名单列表") {
            return
        }
        if (BlackListConfig.blackList.size != 0) {
            var int = 0
            var str = "名单列表\r"
            for (l in BlackListConfig.blackList) {
                int++
                str += "" + int + "." + l.qid + "，拉黑原因：" + l.cause + "\r"
            }
            event.subject.sendMessage(str)
        } else {
            event.subject.sendMessage("黑名单列表为空！")
        }
    }

}