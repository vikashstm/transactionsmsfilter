package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Format(
    @SerializedName("format")
    var format: String = "",
    @SerializedName("use_sms_time")
    var useSmsTime: Boolean = false
)