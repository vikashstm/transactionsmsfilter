package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class MiscInformation(
    @SerializedName("get_balance")
    var getBalance: List<GetBalance> = listOf()
)