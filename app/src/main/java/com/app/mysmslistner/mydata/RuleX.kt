package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class RuleX(
    @SerializedName("category")
    var category: String = "",
    @SerializedName("income_flag_override")
    var incomeFlagOverride: Boolean = false,
    @SerializedName("value")
    var value: String = ""
)