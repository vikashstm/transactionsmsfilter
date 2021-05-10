package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class ChildField(
    @SerializedName("field")
    var `field`: String = "",
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("value")
    var value: String = ""
)