package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("prefix")
    var prefix: String = ""
)