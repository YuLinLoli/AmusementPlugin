package com.mirai.kotlinUtil

import com.mirai.configs.UserListConfig

class AdminUtil {
    companion object {
        /**
         * 验证是否为管理员
         */
        fun adminCheck(qNumber: Long): Boolean {
            var b = false
            for (l in UserListConfig.adminList) {
                if (qNumber == l) {
                    b = true
                }
            }
            return b
        }

    }
}