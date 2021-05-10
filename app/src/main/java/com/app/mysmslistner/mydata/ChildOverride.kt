package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class ChildOverride(
    @SerializedName("child_field")
    var childField: String = "",
    @SerializedName("deleted")
    var deleted: Boolean = false,
    @SerializedName("incomplete")
    var incomplete: Boolean = false,
    @SerializedName("parent_field")
    var parentField: String = ""
)