package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class ParentMatch(
    @SerializedName("child_override")
    var childOverride: List<ChildOverride> = listOf(),
    @SerializedName("parent_override")
    var parentOverride: List<ParentOverride> = listOf()
)