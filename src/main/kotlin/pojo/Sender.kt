package com.yulin.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sender(
    @SerialName("qid")
    var qid: Long,
    @SerialName("cd")
    var cd: Long
)
