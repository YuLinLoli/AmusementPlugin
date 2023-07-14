package com.yulin.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("sender")
data class Sender(var qid: Long, var cd: Long)
