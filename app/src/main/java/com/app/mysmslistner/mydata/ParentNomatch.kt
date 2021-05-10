package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class ParentNomatch(
    @SerializedName("child_override")
    var childOverride: List<ChildOverrideX> = listOf()
)