package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class PosTypeRules(
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("rules")
    var rules: List<RuleX> = listOf()
)