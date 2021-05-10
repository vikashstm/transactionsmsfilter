package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("value")
    var value: String = ""
)