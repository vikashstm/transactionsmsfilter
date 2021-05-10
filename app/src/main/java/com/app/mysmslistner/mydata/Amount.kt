package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class Amount(
    @SerializedName("create_txn")
    var createTxn: Boolean = false,
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("group_ids")
    var groupIds: List<Int> = listOf(),
    @SerializedName("txn_direction")
    var txnDirection: String = "",
    @SerializedName("value")
    var value: Int = 0
)