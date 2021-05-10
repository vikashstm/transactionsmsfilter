package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Time(
    @SerializedName("group_id")
    var groupId: Int = 0
)