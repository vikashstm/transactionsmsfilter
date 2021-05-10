package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class ContactInfo(
    @SerializedName("format")
    var format: String = "",
    @SerializedName("numbers")
    var numbers: List<String> = listOf(),
    @SerializedName("type")
    var type: String = ""
)