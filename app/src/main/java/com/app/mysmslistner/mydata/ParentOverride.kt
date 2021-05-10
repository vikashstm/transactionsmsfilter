package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class ParentOverride(
    @SerializedName("deleted")
    var deleted: Boolean = false,
    @SerializedName("incomplete")
    var incomplete: Boolean = false
)