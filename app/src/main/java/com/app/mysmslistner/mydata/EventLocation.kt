package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class EventLocation(
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("group_ids")
    var groupIds: List<Int> = listOf()
)