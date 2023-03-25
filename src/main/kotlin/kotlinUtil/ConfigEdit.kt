package com.mirai.kotlinUtil

import com.mirai.configs.UserListConfig

class ConfigEdit {
    companion object {
        /**
         * 群加黑
         */
        fun groupBlackListConfigEdit(groupNumber: Long) {
            UserListConfig.groupBlackList.add(groupNumber)
        }

        /**
         * 人加黑
         */
        fun peopleBlackListConfigEdit(qNumber: Long) {
            UserListConfig.peopleBlackList.add(qNumber)
        }

        /**
         * 添加管理员
         */
        fun AdminListConfigEdit(qNumber: Long) {
            UserListConfig.adminList.add(qNumber)
        }
    }
}