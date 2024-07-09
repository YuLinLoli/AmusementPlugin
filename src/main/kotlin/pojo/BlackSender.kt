package com.yulin.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("blackSender")
data class BlackSender(var qid: Long, var cause: String)
