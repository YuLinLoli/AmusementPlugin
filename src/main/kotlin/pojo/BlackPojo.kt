package com.mirai.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("blackPojo")
data class BlackPojo(var qq: Long, var text: String)
