package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class EventReminderSpan(
    @SerializedName("value")
    var value: Int = 0
)