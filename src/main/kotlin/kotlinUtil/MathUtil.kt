package com.yulin.kotlinUtil

import java.security.SecureRandom

class MathUtil {
    companion object {
        //输入一个0-999的数字，判断是否在该百分比概率，如果判断成功则返回true
        fun probability(int: Int): Boolean {

            val i = SecureRandom().nextInt(1000)
            return when {
                i < int -> {
                    true
                }

                else -> false
            }
        }
    }
}