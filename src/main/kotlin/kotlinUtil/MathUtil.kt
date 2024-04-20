package com.yulin.kotlinUtil

import java.util.*

class MathUtil {
    companion object {
        //输入一个0-999的数字，判断是否在该百分比概率，如果判断成功则返回true
        fun probability(int: Int): Boolean {

            val i = Random().nextInt(1000)
            return when {
                i < int -> {
                    true
                }

                else -> false
            }
        }
    }
}