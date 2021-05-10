package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class CreateTxn(
    @SerializedName("amount")
    var amount: AmountX = AmountX()
)