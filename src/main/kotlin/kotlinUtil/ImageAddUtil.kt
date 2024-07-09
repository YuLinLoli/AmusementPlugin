package com.yulin.kotlinUtil

import com.yulin.AmusementPlugin.save
import com.yulin.config.GroupImageConfig
import com.yulin.pojo.GroupImage

object ImageAddUtil {
    /**
     * 添加一张图片到数据库
     */
    fun imageAdd(group: GroupImage, groupImageList: GroupImageConfig, imageName: String): Int {

        val gi = groupImageList.groupImage
        //判断文件是否有内容，如果没有返回10，让上级创建列表
        if (gi.size == 0) {
            return 10
        }

        for (i in 0 until gi.size) {
            if (gi[i].groupId == group.groupId) {
                for (s in gi[i].imageList) {
                    if (s == imageName) {
                        //如果添加过该表情则返回11
                        return 11
                    }
                }
                gi[i].imageList.add(imageName)
                groupImageList.save()
                return 1
            } else {
                return 10
            }
        }


        return 0
    }
}