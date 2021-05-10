package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Rule(
    @SerializedName("full_name")
    var fullName: String = "",
    @SerializedName("misc_information")
    var miscInformation: MiscInformation = MiscInformation(),
    @SerializedName("name")
    var name: String = "",
    @SerializedName("patterns")
    var patterns: List<Pattern> = listOf(),
    @SerializedName("sender_UID")
    var senderUID: String = "",
    @SerializedName("senders")
    var senders: List<String> = listOf(),
    @SerializedName("set_account_as_expense")
    var setAccountAsExpense: Boolean = false
)