package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Rules(
    @SerializedName("blacklist_regex")
    var blacklistRegex: String = "",
    @SerializedName("min_app_version")
    var minAppVersion: String = "",
    @SerializedName("rules")
    var rules: List<Rule> = listOf(),
    @SerializedName("version")
    var version: String = ""
)