package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Pattern(
    @SerializedName("account_name_override")
    var accountNameOverride: String = "",
    @SerializedName("account_type")
    var accountType: String = "",
    @SerializedName("data_fields")
    var dataFields: DataFields = DataFields(),
    @SerializedName("obsolete")
    var obsolete: Boolean = false,
    @SerializedName("pattern_UID")
    var patternUID: String = "",
    @SerializedName("regex")
    var regex: String = "",
    @SerializedName("reparse")
    var reparse: Boolean = false,
    @SerializedName("set_account_as_expense")
    var setAccountAsExpense: Boolean = false,
    @SerializedName("sms_type")
    var smsType: String = "",
    @SerializedName("sort_UID")
    var sortUID: String = ""
)