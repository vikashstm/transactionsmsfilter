package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class MinDueAmount(
    @SerializedName("group_id")
    var groupId: Int = 0
)