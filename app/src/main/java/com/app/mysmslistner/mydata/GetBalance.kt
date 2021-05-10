package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class GetBalance(
    @SerializedName("account_type")
    var accountType: String = "",
    @SerializedName("contact_info")
    var contactInfo: List<ContactInfo> = listOf()
)