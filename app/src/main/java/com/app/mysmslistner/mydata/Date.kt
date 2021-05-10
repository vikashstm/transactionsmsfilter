package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Date(
    @SerializedName("formats")
    var formats: List<Format> = listOf(),
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("group_ids")
    var groupIds: List<Int> = listOf(),
    @SerializedName("use_sms_time")
    var useSmsTime: Boolean = false
)