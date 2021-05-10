package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class RuleXX(
    @SerializedName("acc_type_override")
    var accTypeOverride: String = "",
    @SerializedName("pos_override")
    var posOverride: String = "",
    @SerializedName("set_no_pos")
    var setNoPos: Boolean = false,
    @SerializedName("txn_type")
    var txnType: String = "",
    @SerializedName("value")
    var value: String = ""
)