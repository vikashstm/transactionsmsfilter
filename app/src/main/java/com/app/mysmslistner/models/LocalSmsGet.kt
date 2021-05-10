package com.app.mysmslistner.models


import com.google.gson.annotations.SerializedName

data class LocalSmsGet(
    @SerializedName("sms")
    var sms: List<Sm> = listOf()
)