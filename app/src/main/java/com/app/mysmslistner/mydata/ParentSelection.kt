package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class ParentSelection(
    @SerializedName("child_field")
    var childField: ChildField = ChildField(),
    @SerializedName("match_type")
    var matchType: String = "",
    @SerializedName("match_value")
    var matchValue: Int = 0,
    @SerializedName("parent_field")
    var parentField: String = ""
)