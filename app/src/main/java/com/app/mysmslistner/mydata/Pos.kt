package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Pos(
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("group_ids")
    var groupIds: List<Int> = listOf(),
    @SerializedName("set_no_pos")
    var setNoPos: Boolean = false,
    @SerializedName("value")
    var value: String = ""
)